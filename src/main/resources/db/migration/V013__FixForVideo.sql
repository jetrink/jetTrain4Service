
# fix all sessionRequest statuses to upper case
UPDATE SessionRequests SET status='PENDING' where status='Pending';
UPDATE SessionRequests SET status='APPROVED' where status='Approved';
UPDATE SessionRequests SET status='DECLINED' where status='Declined';

# fix scheduled session status
UPDATE Sessions SET status='SCHEDULED' where status='scheduled';
UPDATE Sessions SET status='STARTED' where status='started';
UPDATE Sessions SET status='ENDED' where status='ended';

# add opentok session ID to Sessions table
ALTER TABLE `Sessions`
  ADD `videoId` CHAR(72) NULL DEFAULT NULL
COMMENT 'opentok session ID'
  AFTER `subjectId`;
