package model;

import java.util.Objects;

/**
 * Represents one single event from Audit trail.
 *
 * Like:
 * Approval completed                                         --- eventTitle
 * Internal user2 Internal user2 last name, Today at 4:24 PM  --- ownerOfEventAndTime
 * Document name: pramata.docx                                --- description
 */
public class AuditTrailEvent
{
    private String eventTitle;
    private String ownerOfEventAndTime;
    private String description;


    public AuditTrailEvent(String eventTitle)
    {
        this.eventTitle = eventTitle;
    }

    public String getEventTitle()
    {
        return eventTitle;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTrailEvent that = (AuditTrailEvent) o;
        return Objects.equals(eventTitle, that.eventTitle) && Objects.equals(ownerOfEventAndTime, that.ownerOfEventAndTime) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(eventTitle, ownerOfEventAndTime, description);
    }

    @Override
    public String toString()
    {
        return "{" + eventTitle +
               ", " + (ownerOfEventAndTime == null ? "" : ownerOfEventAndTime) +
               ", " + (description == null ? "" : description)
             + "}";
    }
}
