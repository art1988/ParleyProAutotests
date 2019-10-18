package com.parley.testing.aws;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AwsService {

    private static final String TRANSITIVE_S3_BUCKET_NAME = "parley-ui-test-reports";

    private String accessKey;
    private String secretKey;
    private String awsRegion;

    public AwsService(String accessKey, String secretKey, String awsRegion) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.awsRegion = awsRegion;
    }

    private AWSStaticCredentialsProvider awsStaticCredentialsProvider(){
        return new AWSStaticCredentialsProvider( new BasicAWSCredentials(
                accessKey,
                secretKey)
        );
    }

    private AmazonS3 amazonS3Client() {
        ClientConfiguration clientConfiguration = new ClientConfigurationFactory().getConfig();
        clientConfiguration.setUseGzip(true);
        clientConfiguration.useTcpKeepAlive();
        clientConfiguration.useThrottledRetries();

        return AmazonS3ClientBuilder.standard().withRegion(awsRegion)
                .withCredentials(awsStaticCredentialsProvider())
                .withClientConfiguration(clientConfiguration)
                .build();
    }

    public void uploadDocumentToS3(String filePath) {
        File file = new File(filePath);
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String keyName = "screenshots"+date;
        long contentLength = file.length();
        long partSize = 5 * 1024 * 1024;
        AmazonS3 s3Client = amazonS3Client();
        List<PartETag> partETags = new ArrayList<PartETag>();

        // Initiate the multipart upload.
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(TRANSITIVE_S3_BUCKET_NAME, keyName);
        InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
        long filePosition = 0;

        for (int i = 1; filePosition < contentLength; i++) {
            // Because the last part could be less than 5 MB, adjust the part size as needed.
            partSize = Math.min(partSize, (contentLength - filePosition));

            // Create the request to upload a part.
            UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(TRANSITIVE_S3_BUCKET_NAME)
                    .withKey(keyName)
                    .withUploadId(initResponse.getUploadId())
                    .withPartNumber(i)
                    .withFileOffset(filePosition)
                    .withFile(file)
                    .withPartSize(partSize);

            // Upload the part and add the response's ETag to our list.
            UploadPartResult uploadResult = s3Client.uploadPart(uploadRequest);
            partETags.add(uploadResult.getPartETag());

            filePosition += partSize;
        }

        // Complete the multipart upload.
        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(TRANSITIVE_S3_BUCKET_NAME, keyName,
                initResponse.getUploadId(), partETags);
        s3Client.completeMultipartUpload(compRequest);

    }

    private void removeProcessedSrc(String key){
        AmazonS3 s3 = amazonS3Client();
        s3.deleteObject(TRANSITIVE_S3_BUCKET_NAME, key);
    }

}
