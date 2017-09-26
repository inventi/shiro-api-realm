# Shiro Api Realm

Apache shiro api realm intended to be used within preauthenticated microservices environment.

**Usage**
    
  * **Configuration Properties**
    * ```cache.ttl-minutes (user cache ttl in minutes)```
    * ```users.endpoint (user api response)```
  * **Spring Configuration**
    * ```Import EnableShiroApiRealm annotation```
  * **Authorization configuration**
    * ```Use Apache Shiro @RequiresPermissions```
  