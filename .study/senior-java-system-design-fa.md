# سوالات مصاحبه System Design برای Senior Java Developer

مجموعه‌ای دسته‌بندی‌شده از سوالات System Design به همراه پاسخ، مخصوص موقعیت Senior Java Developer. اصطلاحات تخصصی به همان شکل انگلیسی نوشته شده‌اند.

---

## ۱. مبانی System Design

**سوال:** مهم‌ترین attribute هایی که هنگام طراحی یک سیستم بررسی می‌کنید کدام‌اند (scalability، availability، consistency، latency، durability)؟ چطور اولویت‌بندی‌شان می‌کنید؟
**پاسخ:** اولویت‌بندی بستگی به نیاز کسب‌وکار دارد. مثلاً در یک سیستم پرداخت، consistency و durability اولویت اول‌اند، در حالی‌که در یک سیستم پیام‌رسان یا فید خبری، availability و latency مهم‌تر می‌شوند. باید ابتدا requirement های functional و non-functional را از stakeholder ها جمع کرد، سپس trade-off ها را بر اساس CAP theorem و SLA مورد نیاز مشخص کرد.

**سوال:** تفاوت vertical scaling و horizontal scaling چیست؟ چه زمانی هرکدام را انتخاب می‌کنید؟
**پاسخ:** Vertical scaling یعنی افزایش منابع یک node واحد (CPU، RAM قوی‌تر) و ساده‌تر است اما یک سقف physical و single point of failure دارد. Horizontal scaling یعنی افزودن node های بیشتر و توزیع load، که scalability و fault tolerance بهتری می‌دهد اما پیچیدگی‌هایی مثل data partitioning، consistency و network overhead به همراه دارد. برای سیستم‌های بزرگ production معمولاً horizontal scaling ترجیح داده می‌شود.

**سوال:** روند estimation ظرفیت (back-of-the-envelope: QPS، storage، bandwidth) برای یک سیستم جدید را توضیح دهید.
**پاسخ:** ابتدا تعداد کاربر فعال (DAU/MAU) و نسبت read/write را تخمین می‌زنیم، سپس QPS متوسط و peak را محاسبه می‌کنیم (مثلاً DAU × تعداد request در روز ÷ ثانیه‌های روز، ضربدر peak factor). برای storage، حجم هر رکورد را در تعداد رکوردهای روزانه/سالانه ضرب می‌کنیم و replication factor را اعمال می‌کنیم. برای bandwidth، اندازه متوسط response را در QPS ضرب می‌کنیم. این تخمین‌ها به انتخاب تعداد server، نوع database و caching strategy کمک می‌کند.

**سوال:** CAP theorem چیست و چطور روی تصمیمات معماری واقعی تاثیر می‌گذارد؟
**پاسخ:** CAP theorem می‌گوید در یک distributed system، در صورت وقوع network partition نمی‌توان همزمان consistency و availability کامل داشت؛ باید یکی را فدای دیگری کرد (CP یا AP). مثلاً MongoDB و HBase بیشتر گرایش CP دارند، در حالی‌که Cassandra و DynamoDB بیشتر AP هستند. این انتخاب باید بر اساس نیاز business (مثلاً banking نیاز به CP دارد، social feed می‌تواند AP باشد) صورت گیرد.

**سوال:** تفاوت latency و throughput چیست و چطور سیستم را برای هرکدام بهینه می‌کنید؟
**پاسخ:** Latency یعنی زمان پاسخ به یک request واحد و throughput یعنی تعداد request هایی که در واحد زمان پردازش می‌شوند. برای کاهش latency از caching، CDN، connection pooling و کاهش hop های network استفاده می‌شود. برای افزایش throughput از horizontal scaling، asynchronous processing، batching و load balancing استفاده می‌شود. گاهی این دو در تضادند (مثلاً batching throughput را بالا می‌برد اما latency هر request را افزایش می‌دهد).

**سوال:** رویکرد شما در یک مصاحبه System Design از جمع‌آوری requirement تا high-level design و deep dive چیست؟
**پاسخ:** ابتدا requirement های functional و non-functional را روشن می‌کنم و سوال می‌پرسم، سپس capacity estimation انجام می‌دهم، بعد یک high-level design با اجزای اصلی (API، service ها، database، cache، queue) رسم می‌کنم، سپس روی data model و API contract تمرکز می‌کنم، و در نهایت به bottleneck ها و deep dive (مثل sharding، caching strategy، failure handling) می‌پردازم.

---

## ۲. Networking، API ها و ارتباطات

**سوال:** REST، gRPC و GraphQL را مقایسه کنید. برای یک Java-based service کدام را انتخاب می‌کنید؟
**پاسخ:** REST ساده، stateless و widely-adopted است و برای public API مناسب است. gRPC از HTTP/2 و Protocol Buffers استفاده می‌کند، latency پایین‌تر و type-safety بهتری دارد و برای internal service-to-service communication در microservice ها مناسب‌تر است. GraphQL به client اجازه می‌دهد دقیقاً داده مورد نیازش را query کند و برای client هایی با نیازهای متفاوت (mobile vs web) مفید است، اما caching و rate limiting آن پیچیده‌تر است. برای internal Java microservices معمولاً gRPC انتخاب می‌شود.

**سوال:** چطور یک API rate limiter طراحی می‌کنید؟ الگوریتم‌های token bucket، leaky bucket و sliding window را توضیح دهید.
**پاسخ:** Token bucket یک bucket با ظرفیت مشخص دارد که با نرخ ثابت token اضافه می‌کند؛ هر request یک token مصرف می‌کند و burst traffic را تا حدی اجازه می‌دهد. Leaky bucket، request ها را در یک queue با نرخ ثابت پردازش می‌کند و traffic را smooth می‌کند اما burst را اجازه نمی‌دهد. Sliding window با شمارش request ها در یک بازه زمانی متحرک، دقت بیشتری نسبت به fixed window دارد. در Java معمولاً با Redis و Lua script یا کتابخانه‌هایی مثل Resilience4j RateLimiter پیاده‌سازی می‌شود، به‌ویژه برای distributed rate limiting.

**سوال:** تفاوت synchronous و asynchronous communication چیست؟ در یک Java microservice چه زمانی هرکدام مناسب است؟
**پاسخ:** در synchronous، caller منتظر پاسخ می‌ماند (مثل REST call با blocking I/O)، مناسب برای عملیاتی که نیاز به پاسخ فوری دارند. در asynchronous، caller بدون انتظار ادامه می‌دهد و پاسخ بعداً از طریق callback، message queue یا event می‌رسد (مثل Kafka)، مناسب برای عملیات long-running یا decoupling سرویس‌ها. در Java می‌توان با CompletableFuture یا reactive stack (Project Reactor) asynchronous کدنویسی کرد.

**سوال:** چطور API versioning را در یک Java service طولانی‌مدت مدیریت می‌کنید؟
**پاسخ:** روش‌های رایج شامل URI versioning (مثل /v1/orders)، header-based versioning، و content negotiation هستند. باید backward compatibility حفظ شود، deprecated endpoint ها با مدت زمان مشخص اعلام شوند و از Semantic Versioning برای internal library ها استفاده شود. در Spring می‌توان با @RequestMapping های جداگانه یا API Gateway routing این کار را انجام داد.

**سوال:** Load balancer چطور کار می‌کند؟ تفاوت L4 و L7 load balancing چیست؟
**پاسخ:** Load balancer traffic ورودی را بین چند instance توزیع می‌کند تا از overload جلوگیری شود و availability بالا برود. L4 load balancing در سطح transport (TCP/UDP) کار می‌کند و فقط بر اساس IP و port تصمیم می‌گیرد، سریع‌تر است. L7 load balancing در سطح application (HTTP) کار می‌کند و می‌تواند بر اساس URL، header یا cookie routing کند، انعطاف بیشتری دارد اما overhead بیشتری هم دارد.

**سوال:** یک URL shortener (مثل bit.ly) طراحی کنید. چه data store، hashing scheme و caching strategy استفاده می‌کنید؟
**پاسخ:** برای تولید short code می‌توان از base62 encoding روی یک auto-increment ID یا از hashing (مثل MD5 با truncation و collision check) استفاده کرد. Data store می‌تواند یک key-value store مثل DynamoDB یا یک relational database با index روی short code باشد. برای caching، URL های پرترافیک در Redis نگه‌داری می‌شوند تا read latency کم شود. سیستم باید read-heavy design داشته باشد چون تعداد redirect بسیار بیشتر از تعداد create است.

---

## ۳. Database ها و Storage

**سوال:** SQL در مقابل NoSQL: چه فاکتورهایی انتخاب را تعیین می‌کنند و trade-off های consistency و schema flexibility چیست؟
**پاسخ:** SQL برای داده‌های structured با relationship پیچیده و نیاز به strong consistency و ACID transaction مناسب است (مثل سیستم مالی). NoSQL برای scalability بالا، schema flexibility و مدل‌های داده‌ای مثل document، key-value، wide-column مناسب است (مثل MongoDB، Cassandra). NoSQL معمولاً eventual consistency را می‌پذیرد تا availability و partition tolerance بهتری داشته باشد.

**سوال:** چطور schema و indexing strategy را برای یک سیستم high-write (مثل order management) با JDBC/JPA طراحی می‌کنید؟
**پاسخ:** برای high-write باید تعداد index ها را محدود کرد چون هر index خودش write overhead دارد. از composite index بر اساس pattern query استفاده می‌شود، جداول با partitioning (مثل partition بر اساس تاریخ) تقسیم می‌شوند، و از batch insert در JPA (`hibernate.jdbc.batch_size`) برای کاهش round-trip استفاده می‌شود. همچنین می‌توان write را از طریق یک write-optimized table انجام داد و بعد async به read model sync کرد (CQRS).

**سوال:** استراتژی‌های database sharding (range-based، hash-based، directory-based) را توضیح دهید. چطور با Java و connection pool مثل HikariCP این را پیاده می‌کنید؟
**پاسخ:** Range-based sharding داده را بر اساس بازه یک key تقسیم می‌کند (ساده اما ممکن است hotspot ایجاد کند). Hash-based sharding از hash کلید برای توزیع یکنواخت استفاده می‌کند اما resharding سخت‌تر است. Directory-based از یک lookup service برای mapping key به shard استفاده می‌کند و انعطاف بیشتری دارد. در Java معمولاً با یک routing layer یا ShardingSphere و چند DataSource جداگانه (هرکدام با HikariCP pool خودش) این کار پیاده می‌شود.

**سوال:** N+1 query problem در JPA/Hibernate چیست و چطور حل می‌شود؟
**پاسخ:** وقتی یک entity با لیستی از entity های وابسته lazy-load می‌شود، برای هر آیتم لیست یک query جداگانه اجرا می‌شود که منجر به N+1 query می‌گردد. راه‌حل‌ها شامل استفاده از JOIN FETCH در JPQL، @EntityGraph، یا batch fetching (`hibernate.default_batch_fetch_size`) است.

**سوال:** چطور یک سیستم برای database failover و replication (leader-follower) طراحی می‌کنید؟
**پاسخ:** یک leader نوشتن‌ها را می‌پذیرد و follower ها به‌صورت async یا sync آن‌ها را replicate می‌کنند. برای failover از یک health check و leader election (مثلاً با Zookeeper یا ابزارهایی مثل Patroni برای PostgreSQL) استفاده می‌شود تا در صورت خرابی leader، یکی از follower ها promote شود. باید read/write splitting در application layer (مثلاً با Spring's `@Transactional(readOnly=true)` routing به replica) در نظر گرفته شود.

**سوال:** تفاوت optimistic locking و pessimistic locking چیست و چطور هرکدام را با JPA پیاده‌سازی می‌کنید (`@Version`، `SELECT FOR UPDATE`)؟
**پاسخ:** Optimistic locking فرض می‌کند conflict نادر است؛ با یک ستون `@Version` تغییرات هم‌زمان تشخیص داده می‌شوند و در صورت conflict یک `OptimisticLockException` پرتاب می‌شود. Pessimistic locking از ابتدا رکورد را با `SELECT ... FOR UPDATE` قفل می‌کند تا هیچ transaction دیگری نتواند آن را تغییر دهد، مناسب برای high-contention scenario ها اما throughput را کاهش می‌دهد.

**سوال:** یک distributed transaction بین چند microservice با database های جداگانه طراحی کنید (Saga pattern در مقابل 2PC).
**پاسخ:** Two-Phase Commit (2PC) یک consistency قوی می‌دهد اما blocking است و در microservice های مقیاس بزرگ scalability کمی دارد. Saga pattern یک sequence از local transaction هاست که هرکدام یک event منتشر می‌کنند؛ در صورت شکست، compensating transaction ها اجرا می‌شوند (choreography-based با Kafka یا orchestration-based با یک saga orchestrator). Saga معمولاً برای microservices ترجیح داده می‌شود چون loosely-coupled و scalable است.

---

## ۴. Caching

**سوال:** یک caching layer برای یک Java service با read زیاد طراحی کنید. local caching (Caffeine، Guava) را با distributed caching (Redis، Memcached) مقایسه کنید.
**پاسخ:** Local cache در heap همان JVM قرار دارد، latency بسیار پایینی دارد اما بین instance ها sync نیست و در horizontal scaling می‌تواند inconsistency ایجاد کند. Distributed cache (Redis) بین همه instance ها به اشتراک گذاشته می‌شود و consistency بهتری دارد اما یک network hop اضافه می‌کند. راه‌حل رایج ترکیبی است: یک local cache (Caffeine) به‌عنوان L1 برای hot data و Redis به‌عنوان L2.

**سوال:** eviction policy های cache (LRU، LFU، FIFO) را توضیح دهید و بگویید کی هرکدام را انتخاب می‌کنید.
**پاسخ:** LRU (Least Recently Used) آیتم‌هایی که اخیراً استفاده نشده‌اند را حذف می‌کند، مناسب اکثر use case ها. LFU (Least Frequently Used) بر اساس تعداد دفعات استفاده حذف می‌کند، مناسب زمانی که popularity پایدار است. FIFO ساده‌ترین است و بدون توجه به usage pattern، قدیمی‌ترین آیتم را حذف می‌کند، مناسب زمانی که پیچیدگی کمتر مهم‌تر از دقت است.

**سوال:** استراتژی‌های cache-aside، write-through و write-behind چیست؟ trade-off هرکدام؟
**پاسخ:** Cache-aside یعنی application ابتدا cache را چک می‌کند و در صورت miss، از database می‌خواند و cache را update می‌کند؛ ساده اما ممکن است stale data داشته باشد. Write-through یعنی هر write هم‌زمان به cache و database نوشته می‌شود، consistency بهتر اما write latency بیشتر. Write-behind یعنی write ابتدا در cache انجام و بعداً async به database نوشته می‌شود، throughput بالا اما ریسک data loss در صورت crash.

**سوال:** چطور از cache stampede / thundering herd جلوگیری می‌کنید؟
**پاسخ:** با استفاده از تکنیک‌هایی مثل lock/mutex (فقط یک thread اجازه دارد cache را از database پر کند و بقیه منتظر بمانند)، probabilistic early expiration، یا stale-while-revalidate (سرو کردن داده قدیمی هنگام refresh در پس‌زمینه).

**سوال:** چطور distributed cache را با source-of-truth database consistent نگه می‌دارید؟
**پاسخ:** با TTL مناسب برای expire خودکار، invalidation event ها (مثلاً از طریق Kafka وقتی رکورد database تغییر می‌کند) و write-through pattern. برای consistency قوی‌تر می‌توان از event-driven cache invalidation یا Change Data Capture (CDC) با ابزارهایی مثل Debezium استفاده کرد.

---

## ۵. Messaging، Queue ها و پردازش Asynchronous

**سوال:** چه زمانی از message queue (Kafka، RabbitMQ، SQS) به‌جای فراخوانی مستقیم synchronous بین سرویس‌ها استفاده می‌کنید؟
**پاسخ:** زمانی که نیاز به decoupling سرویس‌ها، مدیریت traffic spike، پردازش asynchronous یا اطمینان از تحویل پیام (حتی اگر consumer موقتاً down باشد) وجود دارد. مثلاً پردازش سفارش، ارسال notification، یا event-driven workflow ها.

**سوال:** معماری Kafka را توضیح دهید: partition ها، consumer group ها، offset ها و replication. این‌ها چطور روی ordering guarantee تاثیر می‌گذارند؟
**پاسخ:** یک topic به چند partition تقسیم می‌شود و هر پیام یک offset منحصربه‌فرد در آن partition دارد. Ordering فقط درون یک partition تضمین می‌شود، نه در کل topic. Consumer group به چند consumer اجازه می‌دهد partition ها را بین خودشان تقسیم کنند تا parallel processing انجام شود. Replication (با replication factor) کپی‌هایی از هر partition روی broker های مختلف نگه می‌دارد تا در صورت خرابی یک broker، داده از دست نرود.

**سوال:** یک سیستم event-driven پردازش سفارش با Kafka و Java producer/consumer طراحی کنید. چطور exactly-once را از at-least-once delivery متمایز می‌کنید؟
**پاسخ:** At-least-once یعنی ممکن است یک پیام بیش از یک بار پردازش شود (نیازمند idempotent consumer). Exactly-once semantics در Kafka با idempotent producer و transactional API (`transactional.id`) قابل دستیابی است که تضمین می‌کند هر پیام دقیقاً یک بار در طول pipeline پردازش شود. طراحی باید شامل یک idempotency key (مثل order ID) در database برای جلوگیری از پردازش تکراری باشد.

**سوال:** چطور poison message ها / dead-letter queue را در یک Java consumer مدیریت می‌کنید؟
**پاسخ:** پیام‌هایی که بارها پردازش‌شان با خطا مواجه می‌شود (مثلاً بعد از retry با exponential backoff)، به یک Dead Letter Topic/Queue منتقل می‌شوند تا از blocking شدن کل consumer جلوگیری شود. سپس این پیام‌ها می‌توانند به‌صورت جداگانه بررسی یا reprocess شوند.

**سوال:** backpressure چیست و چطور آن را در یک سیستم reactive Java (Project Reactor، RxJava) پیاده‌سازی می‌کنید؟
**پاسخ:** Backpressure مکانیزمی است که به consumer اجازه می‌دهد نرخ داده دریافتی از producer را کنترل کند تا از overwhelm شدن جلوگیری شود. در Project Reactor با Reactive Streams API، subscriber می‌تواند از طریق `request(n)` تعداد item مورد نیاز را اعلام کند و همچنین با عملگرهایی مثل `onBackpressureBuffer` یا `onBackpressureDrop` رفتار در صورت overflow را کنترل کرد.

---

## ۶. معماری Microservices

**سوال:** trade-off های حرکت از monolith به microservices چیست؟ کی monolith هنوز انتخاب درستی است؟
**پاسخ:** Microservices استقلال deploy، scalability مستقل هر سرویس و انتخاب technology stack متنوع را ممکن می‌کند، اما پیچیدگی operational (monitoring، distributed tracing، network reliability) بالا می‌رود. Monolith برای تیم‌های کوچک، MVP یا زمانی که domain boundary هنوز واضح نیست بهتر است چون development و deployment ساده‌تری دارد.

**سوال:** چطور service discovery و configuration management را مدیریت می‌کنید (Eureka، Consul، Spring Cloud Config)؟
**پاسخ:** Service discovery (مثل Eureka یا Consul) به سرویس‌ها اجازه می‌دهد به‌صورت dynamic یکدیگر را در network پیدا کنند بدون hardcode کردن IP. Spring Cloud Config یک central configuration server فراهم می‌کند که تنظیمات را بدون نیاز به redeploy، به سرویس‌ها می‌دهد (با پشتیبانی از refresh dynamic).

**سوال:** یک زنجیره فراخوانی microservice مقاوم با circuit breaker، retry و timeout طراحی کنید (Resilience4j / Hystrix).
**پاسخ:** Circuit breaker وقتی نرخ خطای یک سرویس از یک threshold عبور کند، به‌صورت موقت فراخوانی‌ها را قطع می‌کند تا سرویس downstream فرصت بهبود پیدا کند (fail-fast). Retry با exponential backoff برای خطاهای گذرا استفاده می‌شود. Timeout جلوی block شدن نامحدود thread ها را می‌گیرد. در Java، Resilience4j این pattern ها را به‌صورت annotation-based یا functional فراهم می‌کند.

**سوال:** چطور یک راه‌حل distributed logging و tracing بین microservice ها طراحی می‌کنید (correlation ID، OpenTelemetry، Zipkin/Jaeger)؟
**پاسخ:** یک correlation ID (trace ID) در ابتدای هر request تولید و در تمام header های بین‌سرویسی propagate می‌شود. OpenTelemetry SDK این trace ها را جمع‌آوری و به backend هایی مثل Jaeger یا Zipkin ارسال می‌کند تا کل مسیر یک request بین سرویس‌ها قابل visualize باشد. لاگ‌ها هم باید این correlation ID را شامل شوند تا در ELK قابل جستجو باشند.

**سوال:** API Gateway pattern و مسئولیت‌های آن (auth، rate limiting، routing) را توضیح دهید.
**پاسخ:** API Gateway یک entry point واحد برای همه client ها فراهم می‌کند که مسئولیت‌هایی مثل authentication/authorization، rate limiting، request routing به سرویس مناسب، response aggregation و SSL termination را متمرکز می‌کند تا این logic از خود microservice ها جدا شود.

**سوال:** چطور shared data model ها را مدیریت می‌کنید و از coupling شدید بین microservice ها جلوگیری می‌کنید؟
**پاسخ:** هر سرویس باید database و data model خودش را داشته باشد (database-per-service). به‌جای share کردن مستقیم entity، از یک contract مشخص (API یا event schema) و ابزارهایی مثل Avro/Protobuf با schema registry استفاده می‌شود تا تغییرات backward-compatible باشند.

**سوال:** یک سیستم notification (email/SMS/push) طراحی کنید که در چند microservice مقیاس‌پذیر باشد.
**پاسخ:** سرویس‌های دیگر یک event (مثل OrderPlaced) به یک topic در Kafka منتشر می‌کنند. یک Notification Service این event ها را consume کرده و بر اساس نوع notification (email/SMS/push) به provider مربوطه (SendGrid، Twilio، FCM) ارسال می‌کند. برای reliability از retry، dead-letter queue و idempotency استفاده می‌شود.

---

## ۷. مفاهیم Distributed Systems

**سوال:** eventual consistency در مقابل strong consistency را با مثال واقعی توضیح دهید.
**پاسخ:** Strong consistency یعنی بلافاصله بعد از یک write، همه read ها آخرین مقدار را می‌بینند (مثل transaction بانکی). Eventual consistency یعنی ممکن است برای مدتی replica های مختلف مقادیر متفاوتی نشان دهند اما در نهایت همگرا می‌شوند (مثل like count در شبکه اجتماعی که تاخیر کوتاهی در sync شدن قابل قبول است).

**سوال:** الگوریتم consensus توزیع‌شده (Raft، Paxos) چیست؟ کجا در عمل استفاده می‌شود (مثلاً Zookeeper، etcd)؟
**پاسخ:** این الگوریتم‌ها به مجموعه‌ای از node ها اجازه می‌دهند حتی در حضور خرابی یا network partition، روی یک مقدار واحد توافق کنند. Raft ساده‌تر از Paxos برای فهمیدن است و در سیستم‌هایی مثل etcd و Consul استفاده می‌شود. Zookeeper از یک الگوریتم مشابه (ZAB) برای leader election و coordination استفاده می‌کند.

**سوال:** چطور یک distributed lock در Java پیاده‌سازی می‌کنید (با Redis، Zookeeper یا روش‌های database-based)؟
**پاسخ:** با Redis می‌توان از دستور `SET key value NX PX ttl` برای گرفتن lock با expiration استفاده کرد (الگوریتم Redlock برای reliability بیشتر). با Zookeeper از ephemeral sequential node ها برای پیاده‌سازی lock استفاده می‌شود. Database-based lock هم می‌تواند با یک unique constraint یا `SELECT FOR UPDATE` پیاده‌سازی شود، اما معمولاً throughput پایین‌تری دارد.

**سوال:** idempotency چیست و چطور API/consumer های idempotent در یک distributed system طراحی می‌کنید؟
**پاسخ:** Idempotency یعنی اجرای چندباره یک عملیات، همان نتیجه یک بار اجرا را دارد. برای API ها معمولاً از یک idempotency key که client ارسال می‌کند استفاده می‌شود و server نتیجه اولین request را cache می‌کند. برای consumer ها، قبل از پردازش پیام، وجود آن در یک جدول processed_messages چک می‌شود یا از upsert به‌جای insert استفاده می‌شود.

**سوال:** مفهوم leader election چیست و چطور برای coordination استفاده می‌شود (مثلاً leader election مبتنی بر Zookeeper در Java)؟
**پاسخ:** Leader election فرآیندی است که در آن یک node از میان چند node به‌عنوان leader انتخاب می‌شود تا عملیات هماهنگ‌سازی‌شده (مثل scheduling یک job) را انجام دهد. در Zookeeper این کار با ایجاد ephemeral sequential znode ها انجام می‌شود؛ node ای که کمترین sequence number را دارد leader می‌شود و در صورت disconnect شدن leader، znode آن حذف و node بعدی leader می‌شود.

**سوال:** چطور clock skew و ordering رویدادها بین node های توزیع‌شده را مدیریت می‌کنید (vector clock، logical clock)؟
**پاسخ:** به‌جای تکیه بر physical clock (که بین node ها sync نیست)، از logical clock مثل Lamport timestamp برای ترتیب نسبی event ها استفاده می‌شود. Vector clock پیشرفته‌تر است و می‌تواند causality بین event ها را در سیستم‌های چند-node تشخیص دهد (که event A قبل از B رخ داده یا concurrent هستند).

---

## ۸. Reliability، Scalability و Observability

**سوال:** یک سیستم با availability ۹۹.۹۹٪ طراحی کنید. چه استراتژی‌های redundancy و failover اعمال می‌کنید؟
**پاسخ:** استفاده از multiple availability zone یا region، redundant instance پشت load balancer، health check و auto-scaling، database replication با automatic failover، و circuit breaker برای جلوگیری از cascading failure. باید همچنین یک disaster recovery plan و chaos engineering (مثل Chaos Monkey) برای تست resilience وجود داشته باشد.

**سوال:** چطور health check و readiness/liveness probe را برای یک Java service در Kubernetes طراحی می‌کنید؟
**پاسخ:** Liveness probe مشخص می‌کند آیا application هنوز زنده است یا باید restart شود (مثلاً endpoint `/actuator/health/liveness` در Spring Boot). Readiness probe مشخص می‌کند آیا application آماده دریافت traffic است (مثلاً بعد از اتصال موفق به database و cache)، و Kubernetes تا زمانی که readiness موفق نباشد traffic ارسال نمی‌کند.

**سوال:** bulkhead pattern را توضیح دهید و بگویید چطور از cascading failure جلوگیری می‌کند.
**پاسخ:** Bulkhead pattern منابع (مثل thread pool یا connection pool) را بین بخش‌های مختلف سیستم isolate می‌کند تا اگر یک بخش دچار overload یا failure شود، بقیه بخش‌ها تحت تاثیر قرار نگیرند؛ شبیه به دیواره‌های ضدآب یک کشتی.

**سوال:** یک سیستم monitoring و alerting برای مجموعه‌ای از Java microservices با Prometheus/Grafana و ELK stack طراحی کنید.
**پاسخ:** هر سرویس metric های خودش (latency، error rate، throughput) را از طریق Micrometer در قالب Prometheus expose می‌کند. Prometheus این metric ها را scrape و ذخیره می‌کند و Grafana برای visualization و alerting استفاده می‌شود. برای log ها، هر سرویس log های structured (JSON) با correlation ID تولید می‌کند که توسط Filebeat/Logstash جمع‌آوری و در Elasticsearch ایندکس شده و در Kibana قابل جستجو می‌شود.

**سوال:** چطور capacity planning و load testing را برای یک Java service قبل از یک رویداد ترافیک بزرگ انجام می‌دهید؟
**پاسخ:** با ابزارهایی مثل JMeter، Gatling یا k6، traffic pattern واقعی شبیه‌سازی می‌شود، bottleneck ها (CPU، DB connection، GC pause) شناسایی و tuning می‌شوند، و بر اساس نتایج، تعداد instance و auto-scaling threshold تنظیم می‌شود.

**سوال:** یک استراتژی graceful degradation برای یک سرویس تحت فشار زیاد طراحی کنید.
**پاسخ:** می‌توان features غیرضروری (مثل recommendation یا analytics) را موقتاً غیرفعال کرد، از cached یا stale data به‌جای real-time data استفاده کرد، یا با load shedding، درخواست‌های کم‌اولویت را رد کرد تا core functionality در دسترس بماند.

---

## ۹. امنیت (Security)

**سوال:** چطور authentication و authorization را برای یک معماری microservices طراحی می‌کنید (OAuth2، JWT، API key)؟
**پاسخ:** یک Identity Provider (مثل Keycloak یا Auth0) با پروتکل OAuth2/OIDC، token صادر می‌کند. JWT به‌عنوان access token بین سرویس‌ها propagate می‌شود و هر سرویس می‌تواند آن را بدون round-trip اضافه به identity provider، verify کند (با public key). API Gateway معمولاً authentication اولیه را انجام می‌دهد و authorization دقیق‌تر (role/scope-based) در خود سرویس‌ها بررسی می‌شود.

**سوال:** چطور ارتباط service-to-service در microservice های Java را امن می‌کنید (mTLS)؟
**پاسخ:** با mutual TLS، هر دو طرف (client و server) certificate خود را ارائه می‌دهند و یکدیگر را احراز هویت می‌کنند. در Kubernetes معمولاً این کار با یک service mesh مثل Istio یا Linkerd به‌صورت خودکار (بدون تغییر کد Java) انجام می‌شود.

**سوال:** چطور از vulnerability های رایج (SQL injection، XSS، CSRF) در یک اپلیکیشن Spring-based جلوگیری می‌کنید؟
**پاسخ:** برای SQL injection از prepared statement و JPA/Hibernate (به‌جای string concatenation) استفاده می‌شود. برای XSS، ورودی و خروجی sanitize و encode می‌شود (مثلاً Thymeleaf به‌صورت پیش‌فرض auto-escape می‌کند). برای CSRF، Spring Security از CSRF token برای state-changing request ها استفاده می‌کند.

**سوال:** یک روش امن برای مدیریت secrets (API key، DB credential) طراحی کنید (Vault، AWS Secrets Manager).
**پاسخ:** Secrets هرگز نباید در کد یا config file plaintext ذخیره شوند. از ابزارهایی مثل HashiCorp Vault یا AWS Secrets Manager استفاده می‌شود که secrets را encrypted نگه می‌دارند و application در runtime آن‌ها را با یک short-lived token دریافت می‌کند، همراه با rotation دوره‌ای.

---

## ۱۰. موضوعات تخصصی Java (Concurrency، JVM و Performance)

**سوال:** Java Memory Model (JMM) را توضیح دهید و بگویید `volatile`، `synchronized` و `final` چطور با آن تعامل دارند.
**پاسخ:** JMM قوانینی تعریف می‌کند که مشخص می‌کند تغییرات یک thread چه زمانی برای thread های دیگر قابل مشاهده است. `volatile` تضمین می‌کند خواندن/نوشتن مستقیم به main memory انجام شود (visibility) اما atomicity کامپوند عملیات را تضمین نمی‌کند. `synchronized` هم visibility و هم mutual exclusion (atomicity) فراهم می‌کند از طریق monitor lock. `final` field ها اگر درست initialize شوند، بعد از constructor بدون نیاز به synchronization اضافی برای thread های دیگر safely visible هستند.

**سوال:** `ExecutorService`، `CompletableFuture` و virtual thread (Project Loom) را برای مدیریت concurrent workload مقایسه کنید.
**پاسخ:** `ExecutorService` یک thread pool سنتی است که task ها را روی تعداد ثابتی platform thread اجرا می‌کند؛ برای workload های blocking I/O زیاد می‌تواند منجر به thread starvation شود. `CompletableFuture` امکان composition غیرهمزمان (chaining، combining) عملیات را فراهم می‌کند. Virtual thread (Project Loom) thread های سبک JVM-managed هستند که امکان ایجاد میلیون‌ها thread بدون overhead سنگین OS thread را می‌دهند، بسیار مناسب برای workload های I/O-bound با کد blocking-style ساده.

**سوال:** یک سیستم producer-consumer با throughput بالا و thread-safe در Java طراحی کنید.
**پاسخ:** با استفاده از `BlockingQueue` (مثل `LinkedBlockingQueue` یا `ArrayBlockingQueue`) که به‌صورت داخلی thread-safe است، چند producer thread داده تولید کرده و در queue قرار می‌دهند و چند consumer thread از queue مصرف می‌کنند. برای throughput بالاتر می‌توان از `Disruptor` (LMAX) که یک ring buffer با کمترین contention است استفاده کرد.

**سوال:** استراتژی‌های garbage collection در JVM (G1، ZGC، Shenandoah) را بررسی کنید. چطور GC را برای یک سرویس low-latency tune می‌کنید؟
**پاسخ:** G1 GC برای اکثر application ها با heap متوسط تا بزرگ مناسب است و pause time هدفمند دارد. ZGC و Shenandoah برای heap های خیلی بزرگ و latency پایین (زیر ۱۰ میلی‌ثانیه pause) طراحی شده‌اند چون بیشتر کار GC را concurrent با application انجام می‌دهند. Tuning شامل تنظیم heap size مناسب، انتخاب collector مناسب، و monitoring GC log ها برای شناسایی pause های طولانی است.

**سوال:** چطور یک memory leak یا thread contention issue را در یک Java application در production تشخیص و رفع می‌کنید؟
**پاسخ:** با ابزارهایی مثل heap dump (`jmap`) و تحلیل با Eclipse MAT، می‌توان object هایی که به‌اشتباه reference نگه‌داشته‌اند (مثل static collection که پاک نمی‌شود) را پیدا کرد. برای thread contention، از thread dump (`jstack`) یا profiler هایی مثل async-profiler برای شناسایی lock contention و hot method ها استفاده می‌شود.

**سوال:** تفاوت `ConcurrentHashMap`، `synchronizedMap` و `CopyOnWriteArrayList` چیست و کی هرکدام را استفاده می‌کنید؟
**پاسخ:** `ConcurrentHashMap` از segment-based (یا bucket-based در نسخه‌های جدید) locking برای concurrency بالا با read بدون lock استفاده می‌کند، مناسب read/write زیاد. `synchronizedMap` کل map را با یک lock واحد محافظت می‌کند که throughput پایین‌تری در concurrency بالا دارد. `CopyOnWriteArrayList` هر write یک کپی جدید از آرایه می‌سازد؛ مناسب برای read بسیار زیاد و write نادر (مثل لیست listener ها).

**سوال:** چطور یک connection pool را از صفر طراحی می‌کنید و از چه concurrency primitive هایی استفاده می‌کنید؟
**پاسخ:** یک pool از connection های از پیش‌ساخته در یک `BlockingQueue` نگه‌داری می‌شود. وقتی client یک connection درخواست می‌کند، از queue برداشته می‌شود (با timeout در صورت خالی بودن) و بعد از استفاده به queue برگردانده می‌شود. باید health check دوره‌ای برای connection های idle/stale، حداکثر و حداقل pool size، و مدیریت leak (اگر connection برنگردد) در نظر گرفته شود.

---

## ۱۱. مطالعات موردی End-to-End System Design

**سوال:** یک URL shortener (bit.ly) طراحی کنید — از API تا storage تا caching.
**پاسخ:** جزئیات کامل در بخش ۲ (Networking) آمده است: base62 encoding برای short code، key-value store برای mapping، Redis برای cache کردن URL های پرترافیک، و طراحی read-heavy با replica های متعدد برای redirect service.

**سوال:** یک distributed rate limiter طراحی کنید که بین چند instance از API gateway استفاده شود.
**پاسخ:** به‌جای نگه‌داری counter در حافظه هر instance (که باعث inconsistency می‌شود)، از یک central store مثل Redis با اتمی بودن عملیات (`INCR` + `EXPIRE` یا Lua script برای token bucket) استفاده می‌شود تا همه instance ها یک counter مشترک را ببینند. برای کاهش latency می‌توان از local cache با sync دوره‌ای هم استفاده کرد (trade-off بین دقت و performance).

**سوال:** یک سیستم پردازش سفارش e-commerce با مدیریت موجودی (inventory) مقیاس‌پذیر طراحی کنید.
**پاسخ:** یک Order Service سفارش را دریافت و یک event (OrderCreated) منتشر می‌کند. Inventory Service موجودی را با یک عملیات atomic (مثل optimistic locking یا Redis `DECR`) کاهش می‌دهد. اگر موجودی کافی نبود، یک compensating event (OrderFailed) منتشر می‌شود (Saga pattern). Payment Service به‌صورت جداگانه پرداخت را پردازش می‌کند و در صورت شکست، inventory دوباره افزایش می‌یابد.

**سوال:** یک اپلیکیشن chat بلادرنگ (real-time) طراحی کنید (WebSocket، ordering پیام‌ها، delivery guarantee).
**پاسخ:** از WebSocket برای ارتباط دوطرفه real-time بین client و server استفاده می‌شود. برای scale کردن به چند instance، از یک message broker (مثل Redis Pub/Sub یا Kafka) برای route کردن پیام بین instance های مختلف server استفاده می‌شود. Ordering با یک sequence number per-conversation تضمین می‌شود و delivery guarantee با ack از سمت client و ذخیره پیام‌های unread در database تامین می‌شود.

**سوال:** یک سیستم news feed / social media timeline طراحی کنید (fan-out on write در مقابل fan-out on read).
**پاسخ:** Fan-out on write یعنی هنگام post کردن، پیام بلافاصله به timeline همه follower ها نوشته می‌شود؛ سریع برای read اما برای کاربران با میلیون‌ها follower (celebrity) پرهزینه است. Fan-out on read یعنی timeline در لحظه request از posts افرادی که follow شده‌اند aggregate می‌شود؛ برای write سبک‌تر اما read کندتر است. راه‌حل رایج ترکیبی است: fan-out on write برای اکثر کاربران و fan-out on read برای celebrity ها.

**سوال:** یک distributed job scheduler (مثل Quartz در مقیاس بزرگ) طراحی کنید که از duplicate execution یک job جلوگیری کند.
**پاسخ:** از یک distributed lock (مثل Zookeeper یا Redis) قبل از اجرای هر job استفاده می‌شود تا فقط یک instance آن را اجرا کند. Job های scheduled در یک database مرکزی با وضعیت (pending/running/completed) نگه‌داری می‌شوند و یک leader election بین scheduler instance ها مشخص می‌کند کدام instance مسئول trigger کردن job هاست.

**سوال:** یک سیستم پردازش پرداخت با نیازمندی‌های consistency قوی و auditability طراحی کنید.
**پاسخ:** از ACID transaction در سطح database برای عملیات‌های critical مثل کسر موجودی استفاده می‌شود. هر تراکنش یک idempotency key دارد تا از double-charging جلوگیری شود. یک audit log (event sourcing) تمام تغییرات state را immutable ذخیره می‌کند تا قابلیت traceability و reconciliation کامل وجود داشته باشد. برای ارتباط با payment gateway خارجی از retry با idempotency و reconciliation job دوره‌ای استفاده می‌شود.

**سوال:** یک سرویس file storage/upload (مثل Dropbox) با chunking، dedup و مدیریت metadata طراحی کنید.
**پاسخ:** فایل‌های بزرگ به chunk های کوچک‌تر تقسیم می‌شوند تا آپلود resume-able و parallel باشد. هر chunk یک hash (مثل SHA-256) دارد که برای deduplication استفاده می‌شود (اگر chunk قبلاً وجود دارد، دوباره ذخیره نمی‌شود). Metadata (نام فایل، owner، لیست chunk ها) در یک database جداگانه نگه‌داری می‌شود در حالی‌که خود chunk ها در یک object storage مثل S3 ذخیره می‌شوند.

---

### نحوه استفاده از این راهنما
- **برای مصاحبه‌کننده‌ها:** بسته به focus نقش (مثلاً تمرکز بیشتر روی messaging/microservices برای نقش backend platform، یا تمرکز روی JVM internals برای نقش performance-critical)، از هر دسته ۱ تا ۲ سوال انتخاب کنید.
- **برای کاندیدها:** از هر دسته به‌عنوان یک checklist مطالعه استفاده کنید — آماده باشید حداقل یک case study را کامل روی whiteboard طراحی کنید و trade-off هر تصمیم را توجیه کنید.
