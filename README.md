# Shiro Api Realm

Apache shiro api realm intended to be used within preauthenticated microservices environment.

**Usage**
    
  * **Configuration Properties**
    * ```cache.ttl-minutes (user cache ttl in minutes)```
    * ```users.endpoint (user api endpoint)```
  * **Spring Configuration**
    * ```Add EnableShiroApiRealm annotation```
  * **Authorization configuration**
    * ```Use Apache Shiro @RequiresPermissions```
 
**Audit**
  
  Publishes specified endpoint auditing information to kafka topic
  ```
    {
      "eventId": "09cf3eba-9748-4610-9fb5-f5a4648578f0",
      "dateTime": "2017-10-06T12:59:23.947",
      "user": {
        "userName": "Fred",
        "agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36",
        "location": "192.133.122.222" (location is set from `X-Forwarded-For` header)
      },
      "action": {
        "name": "MethodName",
        "server": "server:8080",
        "uri": "/v1/data",
        "query: "from=122&to=344"
        "method": "GET",
        "status": 200
      }
    }
  ```
  
  * **Configuration Properties**
    * ```audit.event-topic (kafka topic where to publish events)```
    * ```audit.kafka-bootstrap-servers```
  * **Spring configuration**
    * ```add @EnableAudit annotation``` 
 
 For endpoint to generate audit information request needs to contain  header `x-credential-username`
 and method must be annotated with `@RequiresPermissions` or `@AuditAction`
