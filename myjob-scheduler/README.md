**1.	Overview**

MYJOB is a distributed task scheduling framework, the core design goal is to develop quickly, lightweight, easy to expand. The job detail is persisted, and the framework is scalable to thousands of jobs and many executors.

**2.	Features**
- Simple: support through the Web page on the task CRUD operation, simple operation, a minute to get started;
- Dynamic: support dynamic modification of task status, start/ stop tasks, one time execution tasks, immediate effect;
- (TODO)Task Failover: Deploy the Executor cluster,t asks will be smooth to switch executor when the strategy of the router choose ‘failover’;
- (TODO)Custom task parameters: support online configuration scheduling tasks into the parameters, immediate effect;
- (TODO)Mail alarm: the task fails to support e-mail alarm, support configuring multiple email addresses to send bulk alert messages;
- Status monitoring: support real-time monitoring of the progress of the task;
- (TODO: Just simple support Random) Router strategy: A rich routing strategy is provided when the executor cluster is deployed, these include: first, last, poll, random, consistent HASH, least frequently used, least recently used, etc.
- (TODO: Just simple support Retry) Failure handling strategy: Handling strategy when scheduling fails, the strategy includes: failure alarm (default), failure retry;
