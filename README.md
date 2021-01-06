# Foreword

This workshop overall contains **three sections**, feel free to skip a section if you already know how it works:

- Using **Docker** Containers (starts from [Installing Docker](#Installing Docker))
- Using **MySQL** as Database (starts from [Using MySQL](#Using MySQL))
- Using **Spring** as Backend Server (starts from [Using Spring as Backend](#Using Spring as Backend))

# Installing Docker

For **Linux** Users:

- For this tutorial, you need to **install**:

  ```bash
  sudo apt install docker.io
  ```

  ```bash
  sudo apt install docker-compose
  ```

- then, to make sure `docker` daemon is running, run:

  ```
  systemctl start docker
  ```

For **Windows CMD** Users:

> **Downloads and Prerequisites:**
>
> - download docker at https://www.docker.com/products/docker-desktop

1. Run the `Docker` application you just downloaded. Once it is loaded, you should be good to go.
2. Verify that `docker` is running by the command `docker ps -a`

For **Windows WSL** Users:

> **Downloads and Prerequisites:**
>
> - download docker at https://www.docker.com/products/docker-desktop
>
> - make sure you have `WSL 2` instead of `WSL 1`.
>
>   - to see which version you have, try in `CMD`:
>
>     ```bash
>     wsl -l -v
>     ```
>
>       - to upgrade your existing Linux distro to v2, run:
>
>     ```
>    wsl --set-version (distro name) 2
>     ```

1. Run the `Docker` application you just downloaded

2. Configure your docker with the following options for WSL2 integration

   <img src=".\images\Snipaste_2021-01-04_17-00-51.png" style="zoom: 67%;" />

   and then:

   <img src=".\images\Snipaste_2021-01-04_17-01-31.png" style="zoom:67%;" />

3. Now, you can use your WSL and do:

   ```bash
   docker ps -a
   ```

   - if you are in a Windows directory, you might need to run `sudo docker ps -a`

# Docker Containers

> **Downloads and Prerequisites**:
>
> - Before pulling images from docker hub, you might need to login with
>
>   ```bash
>   docker login
>   ```
>   
>   If you do not yet have an account, please register one at https://hub.docker.com/.

**Goal:**

- Understand what a container is
- How to setup and run a docker container
- How to configure the container
  - exposing container ports
  - data persistence

## Setting up MySQL and Redis Containers

> **Downloads and Prerequisites:**
>
> - A working `docker` command line tool (see [Installing Docker](#Installing Docker))

**Goal:**

- Understand what an *image* and a *container* is
- Setup and run a `MySQL` docker container
  - the procedure for setting up a `Redis` docker container is extremely similar, so it is not covered here

**Quick Guide:**

1. First, we need to pull `mysql` and `redis` *images* (you can think of it like the ISO image for building up an OS) with their latest distribution

   ```bash
   docker pull redis
   ```

   ```bash
   docker pull mysql/mysql-server
   ```

   where:

   - the above defaults the image version to `latest`. If you want to have a different version

   Once they are ready, verify it with `docker images`:

   ```bash
   jasonyux@XY-Laptop /code $ docker images
   REPOSITORY           TAG                 IMAGE ID            CREATED             SIZE
   redis                latest              ef47f3b6dc11        3 weeks ago         104MB
   mysql/mysql-server   latest              c8552d79a138        2 months ago        405MB
   ```

2. Now, to set up a container, simply *build it from the images* you just pulled, with:

   ```bash
   docker run --name=<your-container-name> <image-you-have>
   ```

   *For Example:*

   ```bash
   jasonyux@XY-Laptop /code $ docker run --name=test-mysql mysql/mysql-server
   [Entrypoint] MySQL Docker Image 8.0.22-1.1.18
   [Entrypoint] No password option specified for new database.
   [Entrypoint]   A random onetime password will be generated.
   [Entrypoint] Initializing database
   ... [a bunch of other logs]
   [Entrypoint] GENERATED ROOT PASSWORD: G0SBAr.Yf64hoDW@g4w9EKikosA
   ... [a bunch of other logs]
   2021-01-06T09:23:38.394274Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.22'  socket: '/var/lib/mysql/mysql.sock'  port: 3306  MySQL Community Server - GPL.
   ```

   where:

   - take a note at the **generated password**: `G0SBAr.Yf64hoDW@g4w9EKikosA`. This will be used to connect to the `MySQL` database later.

   At this point, this *container* is already up an running like a standalone virtual machine with configured `MySQL`!

   > **Tip:**
   >
   > - To see which containers are running, use `docker ps`
   > - To see all the containers you have, use `docker ps -a`

3. To interact with this container, **open another terminal** and execute:

   ```bash
   docker exec -it <your-container-name> <command>
   ```

   *For Example: To Enter the Container's bash*

   ```bash
   jasonyux@XY-Laptop /code $ docker exec -it test-mysql bash
   bash-4.2# 
   ```

   At this point, you can basically treat this container as a virtual machine, you can do `ls` to look at the file structure, and explore around inside the container.

4. Now, to connect with the `MySQL` database, execute `mysql -uroot -p` inside the `bash` of the container:

   ```bash
   bash-4.2# mysql -uroot -p
   Enter password:
   ```

   where:

   - here we need to use the generated password `G0SBAr.Yf64hoDW@g4w9EKikosA` in step 2.

   Then, if everything works out, you should be brought to the `MySQL CLI` :

   ```bash
   Welcome to the MySQL monitor.  Commands end with ; or \g.
   Your MySQL connection id is 29
   Server version: 8.0.22
   
   Copyright (c) 2000, 2020, Oracle and/or its affiliates. All rights reserved.
   
   Oracle is a registered trademark of Oracle Corporation and/or its
   affiliates. Other names may be trademarks of their respective
   owners.
   
   Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.
   
   mysql>
   ```

   Now, if you know some `SQL` queries, you can try them out here.

   Once you are done, to exit `MySQL` and the container, run `exit` **twice**:

   ```bash
   mysql> exit
   Bye
   bash-4.2# exit
   exit
   jasonyux@XY-Laptop /code $ // back at my laptop
   ```

   Once you are done, you can also *terminate the container that ran a bunch of logs as well*:

   ```bash
   jasonyux@XY-Laptop /code $ docker stop test-mysql
   test-mysql
   ```

   where:

   - using `docker stop` stops the container *gracefully*, this means that you might need to wait for some time until it finishes the clean up
   - you can also use `docker kill test-mysql` instead, which immediately shuts down the container

5. At this point, you should kind of have an idea *what a container can do*: it essentially can be customized to provide anything you need, plus it is easily portable and has a very good isolation (e.g. from other containers or from your own file system).

However, this is **not yet satisfactory**:

- I would like to be able to run the containers in background
- I would like to be able to connect to the container remotely
- I would like my data to be stored persistently

## Running Containers in Background

> **Downloads and Prerequisites:**
>
> - Knows the basics of how to run a container from its image

**Goal:**

- Run the containers in background
- Configure the container to forward its port

**Quick Guide:**

1. To run the container in **background,** you need to use the `--detach`

   *For Example:*

   ```bash
   jasonyux@XY-Laptop /code $ docker run --detach --name=test-mysql mysql/mysql-server
   a74e91a5bdc3f6364bb97f704c7bd1b00bc43b75edc708a66df2262af7d938a5
   ```

   where:

   - if you followed the guide from the previous section, you need to be careful of the **name conflicts for the container** (i.e. you cannot have two containers of the same `--name=test-mysql`)
     - in this example, I removed my previous container with `docker container rm test-mysql`

   Now, if you use `docker ps`, you should see the container is up and running, and you can interact with it again with:

   ```bash
   docker exec -it test-mysql bash
   ```

2. Now, a **new problem** occurs. If I am running as the detached state, where can I find my generated `MySQL` password?

   In fact, there is a handy command to peak into the container's log, which is `docker logs`.

   *For Example:*

   ```bash
   docker logs test-mysql
   ```

   then you can find the generated password once again.

3. The **last problem** to solve is to make the container exposed to outside traffic (so that your frontend/backend application can connect to it)

   By default, a container is "closed". It is isolated from the outside. To dig a tunnel between the container and the outside world, you need to use `--publish <host-port>:<container-port>` to do **port-forwarding** to your laptop.

   *For Example:*

   ```bash
   jasonyux@XY-Laptop /code $ docker run --publish 3306:3306 --detach --name=test-mysql mysql/mysql-server
   ```

   where:

   - `3306` is the default port of the `MySQL` application inside the container.
   - here, I chose to use the same port in my laptop. You are free to use any unused port.

   Now, if you run `netstat -lnt`, you can see:

   ```bash
   jasonyux@XY-Laptop /code $ netstat -lnt
   Active Internet connections (only servers)
   Proto Recv-Q Send-Q Local Address           Foreign Address         State
   tcp6       0      0 :::3306                 :::*                    LISTEN
   ```

At this point, if you are in a hurry, you could:

- jump right to the section [Using Spring as Backend](#Using Spring as Backend)
- if you know your own backend stuff, connect to and use this containerized `MySQL` right away!

However, you should bare in mind **one last problem**:

- because the containers are also isolated, its file system is also "isolated" from your laptop's file system! This means that if you `stop/kill` the containers, and then restart the docker application, all your **data inside the container might be gone**!
- to solve this issue, you can setup a volume mount, which is covered in the next section.

## Persistence in Containers

> **Downloads and Prerequisites:**
>
> - Knows the basics of how to run a container from its image

**Goal:**

- Configure a volume mount of your container, so that data will be persisted even if your shutdown and reboot everything

**Quick Guide**

1. **In short**, you need to specify: `-v ` or `--mount` option to mount some data inside your container to your laptop's container

   *For Example: Persisting your MySQL Data*

   ```bash
   docker run -v /code/testMount:/var/opt/mysql --detach --publish 3306:3306 --name=test-mysql mysql/mysql-server
   ```

   where:

   - `-v /code/testMount:/var/lib/mysql` means I am mounting/linking data:
     - from the container's directory `/var/lib/mysql` (this is the directory inside the container that **`MySQL` stores its data**)
     - to my laptop's directory `/code/testMount`
   - this mount actually creates a **"two-way link"**. If one day you accidentally deleted your `MySQL` container, you can easily recover all your setup instantly with the same data using the same option `-v /code/testMount:/var/opt/mysql`

   Now, you should see a bunch of files in your `/code/testMount` directory:

   ```bash
   jasonyux@XY-Laptop /code/testMount $ ls
   '#ib_16384_0.dblwr'   auto.cnf        binlog.index   client-cert.pem   ib_logfile0   ibtmp1      mysql.sock           private_key.pem   server-key.pem   undo_001
   '#ib_16384_1.dblwr'   binlog.000001   ca-key.pem     client-key.pem    ib_logfile1   mysql       mysql.sock.lock      public_key.pem    sys              undo_002
   '#innodb_temp'        binlog.000002   ca.pem         ib_buffer_pool    ibdata1       mysql.ibd   performance_schema   server-cert.pem   test
   ```

Now, no matter what you do, for example, removing this container you just setup, you can always **recover the data** by starting another container with the same mount `-v /code/testMount:/var/opt/mysql`.

- additionally, this also means you can setup multiple `MySQL` container working with the same sets of data synchronously!

# Docker Compose

All the above setup has been quite cumbersome: you need to specify a bunch of options, including `--publich`, `-v` for mounting, and etc. Is there a way to quickly setup and configure the containers? The answer is to use `docker-compose`

> **Downloads and Prerequisite:**
>
> - Prepare the `docker-compose.yml` file, either
>   - from this GitHub repository
>   - if you know how it works, write your own `yml`

**Goal:**

- Knows how to use `docker-compose` to quickly setup a container

## Using Docker Compose

**Goal:**

- Have a brief understanding of what `docker-compose.yml` is
- Setup a container with `docker-compose`

Since there is quite a lot going on for configuring your `docker-compose.yml`, I decided to skip explaining how each option works, but give you a brief overview of what I have written:

- Setting up `Redis` (`yml` file located under `docker/redis`)

  ```yml
  version: "3.0"
  services:
    cust-redis:
      image: redis:latest
      volumes:
        - .:/data
        - ./redisConf:/usr/local/etc/redis
      container_name: cust-redis
      ports:
        - "6379:6379"
      command: bash -c "touch /usr/local/etc/redis/redis.conf && redis-server /usr/local/etc/redis/redis.conf"
      tty: true
  ```

- Setting up `MySQL` (`yml` file located under `docker/mysql)`

  ```bash
  version: "3.0"
  services:
      cust-mysql:
          image: mysql/mysql-server
          volumes:
              - ./data:/var/lib/mysql
          container_name: cust-mysql
          ports:
              - "3306:3306"
          tty: true
  ```

  A bunch of options should be straight-forward:

  - `image: mysql/mysql-server` specifies which image I am using
  - `-volumes` specify which volume mounting I am using
  - `container_name: cust-mysql` specifies the container name
  - `ports:` specify which ports I am forwarding

**Quick Guide**

1. Navigate to the directory that contains the `docker-compose.yml` file

2. Run `docker-compose up` 

   *For Example:*

   If the `docker-compose` up is in the `/code/testMySQL` directory:

   ```bash
   jasonyux@XY-Laptop /code/testMySQL $ docker-compose up
   Recreating cust-mysql ... done
   Attaching to cust-mysql
   cust-mysql    | [Entrypoint] MySQL Docker Image 8.0.22-1.1.18
   cust-mysql    | [Entrypoint] No password option specified for new database.
   ...[ logs omitted]
   cust-mysql    | [Entrypoint] GENERATED ROOT PASSWORD: 8YPIjjUx;Uk=0J3G@hoM@h8Ik-3
   ...[ logs omitted]
   cust-mysql    | 2021-01-06T11:10:58.156449Z 0 [System] [MY-010931] [Server] /usr/sbin/mysqld: ready for connections. Version: '8.0.22'  socket: '/var/lib/mysql/mysql.sock'  port: 3306  MySQL Community Server - GPL.
   ```

   where:

   - you might want to remember the generated password `8YPIjjUx;Uk=0J3G@hoM@h8Ik-3`

3. Now, you can terminate this container with `Ctrl-C`, and if you run `docker ps -a`, you will see a fully configured `cust-mysql` ready to go

   *For Example:*

   ```bash
   jasonyux@XY-Laptop /code/testMySQL $ docker ps -a
   CONTAINER ID        IMAGE                COMMAND                  CREATED             STATUS                       PORTS               NAMES
   26a3f183f85f        mysql/mysql-server   "/entrypoint.sh mysq…"   8 minutes ago       Exited (137) 2 minutes ago                       cust-mysql
   ```

# Using MySQL

> **Downloads and Prerequisite**
>
> - A ready to run MySQL container in docker
>   - either set up from the previous section using `docker-compose`
>   - or set up manually using `docker run --publish ...`

**Goal:**

- Knows how to login and use a `MySQL` container
- Knows some basics of `MySQL` queries

## Setting up MySQL Container

**Goal:**

- Knows how to login and change `MySQL` password

**Quick Guide**

1. Use `docker container start cust-mysql` to start the containers

2. You need to configure the **MySQL root password**. (you can skip this step if you want to keep the generated password)

   Run `docker logs <container-name>` to see the logs.

   - for the docker-compose we did above, use `docker logs cust-mysql`

   To see the generated password amongst the logs, run:

   ```bash
   docker logs cust-mysql 2>&1 | grep GENERATED
   ```

   then you will see something like:

   ```bash
   [Entrypoint] GENERATED ROOT PASSWORD: TeviRM@kJOMetobizALOj,3HAqD
   ```

   and your generated password is apparent.

   To change the password, first connect to the container's bash:

   ```bash
   docker exec -it cust-mysql bash
   ```

   this should bring you to the bash terminal of the container. Then execute:

   ```bash
   bash-4.2# mysql -uroot -p
   ```

   and fill in the generate password for the prompt. Finally, this will bring you into MySQL CLI, and you can change the password with a query:

   ```mysql
   mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'devfest2021';
   Query OK, 0 rows affected (0.02 sec)
   ```

   where:

   - in this case, `devfest2021` will be my new password

3. To verify everything worked correctly, log out of the MySQL terminal, and get back to the bash terminal of the container. Then you can simply try logging in again with:

   ```bash
   bash-4.2# mysql -uroot -p
   ```

   but this time filling your own password, and you should be brought into the MySQL terminal successfully again.

## Using MySQL Tables

> **Downloads and Prerequisites:**
>
> - You need to have a working `mysql-server` container, so that you can login to the `mysql cli` easily

**Goal:**

- Understand how to create/use a MySQL table

- Understand how to do some basic MySQL queries

**Quick Guide:**

1. First we need to create a new database to put all the data you need.

   To see what databases you already have, run `SELECT VERSION();`

   > The **convention** for MySQL queries are to use uppercase for MySQL related keywords.
   >
   > - for example, use `SELECT` instead of `select` would be preferred even though both works

   Create a new database with `CREATE DATABASE <databasenName>;`.

   *For example:*

   ```bash
   mysql> CREATE DATABASE test;
   Query OK, 1 row affected (0.01 sec)
   
   mysql> SHOW DATABASES;
   +--------------------+
   | Database           |
   +--------------------+
   | information_schema |
   | mysql              |
   | performance_schema |
   | sys                |
   | test               |
   +--------------------+
   5 rows in set (0.00 sec)
   ```

   > **Note:**
   >
   > - If you want to 
   >
   >   ```bash
   >   mysql> CREATE USER 'root'@'172.18.0.1' IDENTIFIED BY 'devfest2021';
   >   Query OK, 0 rows affected (0.01 sec)
   >   
   >   mysql> GRANT ALL PRIVILEGES ON test.* TO 'root'@'172.18.0.1';
   >   Query OK, 0 rows affected (0.01 sec)
   >   ```
   >
   >   

   Now, you switch to the database you just created with `USE <databaseName>`

   *For Example:*

   ```bash
   mysql> USE test;
   Database changed
   ```

2. Create/configure the tables you need to store data in your own database. A complete guide of the options please follow:

   *For Example: Setting up a simple table for Users*

   ```bash
   mysql> CREATE TABLE Users (
       -> userId INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
       -> userName VARCHAR ( 255 ) NOT NULL,
       -> userEmail VARCHAR ( 255 ) NOT NULL,
       -> userRole VARCHAR ( 10 ) NOT NULL DEFAULT 'free');
   Query OK, 0 rows affected (0.05 sec)
   ```

   To make sure this is setup correctly, run:

   ```bash
   mysql> describe Users;
   +-----------+--------------+------+-----+---------+----------------+
   | Field     | Type         | Null | Key | Default | Extra          |
   +-----------+--------------+------+-----+---------+----------------+
   | userId    | int unsigned | NO   | PRI | NULL    | auto_increment |
   | userName  | varchar(255) | NO   |     | NULL    |                |
   | userEmail | varchar(255) | NO   |     | NULL    |                |
   | userRole  | varchar(10)  | NO   |     | free    |                |
   +-----------+--------------+------+-----+---------+----------------+
   4 rows in set (0.00 sec)
   ```

3. Inserting some data into the table:

   *For Example:*

   ```bash
   mysql> INSERT INTO Users (userId, userName, userEmail, userRole) values (1, 'jason', 'jason123@gmail.com', 'premium');
   Query OK, 1 row affected (0.01 sec)
   ```

   where:

   - `Users` is the table to insert into

   In fact, since we have specified the `userId` to be `AUTO_INCREMENT`, and `userRole` having a default value of `free`, we can:

   ```bash
   mysql> INSERT INTO Users (userName, userEmail) values ('bob','bob123@gmail.com');
   Query OK, 1 row affected (0.01 sec)
   ```

   Now, to look at all entries in the able, we can do:

   ```bash
   mysql> select * from Users;
   +--------+----------+--------------------+----------+
   | userId | userName | userEmail          | userRole |
   +--------+----------+--------------------+----------+
   |      1 | jason    | jason123@gmail.com | premium  |
   |      2 | bob      | bob123@gmail.com   | free     |
   +--------+----------+--------------------+----------+
   2 rows in set (0.00 sec)
   ```

4. To update some data in the table, you can use the `update` keyword:

   *For Example:*

   ```bash
   mysql> UPDATE Users set userName='john', userEmail='john123@gmail.com' WHERE userId=1;
   Query OK, 1 row affected (0.01 sec)
   Rows matched: 1  Changed: 1  Warnings: 0
   ```

   then looking at the data:

   ```bash
   mysql> SELECT * from Users;
   +--------+----------+-------------------+----------+
   | userId | userName | userEmail         | userRole |
   +--------+----------+-------------------+----------+
   |      1 | john     | john123@gmail.com | premium  |
   |      2 | bob      | bob123@gmail.com  | free     |
   +--------+----------+-------------------+----------+
   2 rows in set (0.00 sec)
   ```

# Using Spring as Backend

> **Downloads and Prerequisites**
>
> - Install `maven` (please follow the [link](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html))

**Goal:**

- Build a portable server connected to the containerized `MySQL` database

## Maven and Project Structure

**Goal:**

- Have a brief understanding of what `maven` does
- Have a brief understanding of what `Spring` does
- Understand the project structure

> Spring works closely with **Maven**, which is a tool for dependency and project structure control. When you have configured your project into a certain structure (have a look at things are setup in this repository), `maven` can:
>
> - quickly perform a bunch of operations using *goals*
>   - *for example*, compiling all project related code using `mvn compile`, in which `compile ` is the *goal*
> - quickly manage the dependency in your project using `pom.xml` at the top level
> - etc.

> **Spring** is a framework, which basically has a bunch of pre-configured code that allows you to:
>
> - quickly build a backend (even web) server
> - ***configure*** the server's behavior
>   - this is the central theme of most frameworks, which is called the IoC (inversion of control). This means that *you are not actually controlling your application, your are only configuring your application*.

**Quick Guide:**

1. Pull the sample code from this repository, and you should see the following project structure with some annotation:

   ```bash
   │  pom.xml (Project dependency management)
   │
   └─src
       ├─main
       │  ├─java
       │  │  └─com
       │  │      └─example
       │  │          └─adiworkshop (where your code should go)
       │  │              │  AdiWorkshopApplication.java
       │  │              │
       │  │              ├─controller
       │  │              │      CRUDController.java
       │  │              │
       │  │              ├─entity
       │  │              │      User.java
       │  │              │
       │  │              ├─mapper
       │  │              │      CRUDMapper.java
       │  │              │
       │  │              └─service
       │  │                      CRUDService.java
       │  │                      CRUDServiceImpl.java
       │  │
       │  └─resources
       │      │  application.yml (Spring configuration)
       │      │
       │      ├─static
       │      └─templates
       └─test
           └─java
               └─com
                   └─example
                       └─adiworkshop (where your tests should go)
                               AdiWorkshopApplicationTests.java
   ```

   then, if you have your `maven` installed correctly, run the `package` goal:

   ```bash
   mvn package
   ```

   which would do a bunch of work in your terminal, and package your entire Spring application into an executable and portable  `jar`. 

   - Note: If successful, the above command should output *in the end*:

     ```bash
     [INFO]
     [INFO] Results:
     [INFO]
     [INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
     [INFO]
     [INFO]
     [INFO] --- maven-jar-plugin:3.2.0:jar (default-jar) @ adi-workshop ---
     [INFO] Building jar: D:\Dropbox\SEAS2020\Spring Semester\ADI\Hackathon_Workshop\devfest-public\target\adi-workshop-0.0.1-SNAPSHOT.jar
     [INFO]
     [INFO] --- spring-boot-maven-plugin:2.3.2.RELEASE:repackage (repackage) @ adi-workshop ---
     [INFO] Replacing main artifact with repackaged archive
     [INFO] ------------------------------------------------------------------------
     [INFO] BUILD SUCCESS
     [INFO] ------------------------------------------------------------------------
     [INFO] Total time:  10.257 s
     [INFO] Finished at: 2021-01-05T00:01:31+08:00
     [INFO] ------------------------------------------------------------------------
     ```

2. Now, to run the `jar`, we need to first locate the `jar` file.

   At this point, you should have noticed that the project structure becomes:

   ```bash
   ├─src
   │  ├─main
   │  │  ├─java (sub-structure omitted)
   │  │  └─resources (sub-structure omitted)
   │  └─test (sub-structure omitted)
   └─target
   	│  adi-workshop-0.0.1-SNAPSHOT.jar
       │  adi-workshop-0.0.1-SNAPSHOT.jar.original
       ├ (other contents omitted)
   ```

   and so you see that all the compiled code and its `jar` are placed in the newly generated `target` folder. `cd` into that folder, and run `java -jar adi-workshop-0.0.1-SNAPSHOT.jar`, you will see:

   ```bash
   D:\Dropbox\SEAS2020\Spring Semester\ADI\Hackathon_Workshop\devfest-public\target>java -jar adi-workshop-0.0.1-SNAPSHOT.jar
   
     .   ____          _            __ _ _
    /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
   ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
    \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
     '  |____| .__|_| |_|_| |_\__, | / / / /
    =========|_|==============|___/=/_/_/_/
    :: Spring Boot ::        (v2.3.2.RELEASE)
   
   <Bunch of log omitted>
   2021-01-05 00:07:22.261  INFO 88716 --- [           main] c.e.adiworkshop.AdiWorkshopApplication   : Started AdiWorkshopApplication in 4.409 seconds (JVM running for 4.851)
   2021-01-05 00:07:45.308  INFO 88716 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
   2021-01-05 00:07:45.309  INFO 88716 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
   2021-01-05 00:07:45.318  INFO 88716 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 7 ms
   ```

   And if you don't see any error logs here, then congratulations, everything works!

3. Lastly, to test things out, go into your browser/Postman, and try:

   ```html
   http://localhost:8080/test
   ```

   and you will see:

   ```bash
   DevFest 2021
   ```

   And more interestingly:

   ```html
   http://localhost:8080/getAllUsers
   ```

   and you will see your data served up from the database right in front of you in `JSON`:

   ```json
   [
       {
           "userId": "1",
           "userName": "john",
           "userEmail": "john123@gmail.com",
           "userRole": "premium"
       },
       {
           "userId": "2",
           "userName": "bob",
           "userEmail": "bob123@gmail.com",
           "userRole": "free"
       }
   ]
   ```


## Basic CRUD Operations

> **Downloads and Prerequisites**
>
> - A working MySQL database
> - A Spring project with a working connection to the MySQL database

> ***Reminder*:**
>
> - the project structure I use looks like:
>
>   ```bash
>   main
>   │  ├─java
>   │  │  └─com
>   │  │      └─example
>   │  │          └─adiworkshop
>   │  │              │  AdiWorkshopApplication.java
>   │  │              │
>   │  │              ├─controller
>   │  │              │      CRUDController.java (Controller class)
>   │  │              │
>   │  │              ├─entity
>   │  │              │      User.java (DTO class)
>   │  │              │
>   │  │              ├─mapper
>   │  │              │      CRUDMapper.java (Mapper class)
>   │  │              │
>   │  │              └─service
>   │  │                      CRUDService.java (Service Classes)
>   │  │                      CRUDServiceImpl.java
>   (omitted)
>   ```

**Goal:**

- Build a Spring server with some simple CRUD operations

**Quick Guide:**

1. Let us start from some basic CRUD MySQL queries first. In this example, I want to be able to:

   - Create a new user into the `Users` table:

     ```mysql
     INSERT INTO Users (userName. userEmail, userRole) values ('xxx', 'xxxyyy@gmail.com', 'zzz')
     ```

   - Read all users data from the `Users` table:

     ```mysql
     SELECT * FROM Users;
     ```

   - Read a user's email from his/her username:

     ```mysql
     SELECT userEmail FROM Users WHERE userName='xxx';
     ```

   - Update a user's information by user id (e.g. `userId=5`):

     ```mysql
     UPDATE Users set userName='newName', userEmail='newEmail', userRole='newRole' WHERE userId=5;
     ```

   - Delete a user by user id (e.g. `userId=5`):

     ```mysql
     DELETE FROM Users WHERE userId=5;
     ```

2. For simple operations like the ones above, we could use the `MyBatis` dependency in Spring.

   - inside your `Mapper` class, write the following

     ```java
     @Mapper // this annotation is necessary
     public interface CRUDMapper {
         /**
          * MyBatis: Useful when query statements are simple
          * @return Executes the query, amd returns the query result (rows of Users wrapped into List<Users>)
          */
         @Select("SELECT * FROM Users")
         List<User> getAllUsers();
     
         @Select("SELECT userEmail FROM Users WHERE userName=#{username}")
         String getUserEmail(String username);
     
         @Insert("INSERT INTO Users (userName, userEmail, userRole) values " +
                 "(#{userName}, #{userEmail}, #{userRole})")
         Integer createNewUser(User user);
         
         @Update("UPDATE Users set userName=#{userName}, userEmail=#{userEmail}, userRole=#{userRole} " +
                 "WHERE userId=${userId}")
         Integer updateUser(User user);
     
         @Delete("DELETE FROM Users WHERE userId=${userId}")
         Integer deleteUser(Integer userId);
     }
     ```

     where:

     - To **access variables** in the method argument, use `#{}` or `${}`.

       *For Example:*

       ```java
       @Select("SELECT userEmail FROM Users WHERE userName=#{username}")
       String getUserEmail(String username);
       
       @Select("SELECT userEmail FROM Users WHERE userName=${username}")
       String getUserEmailWrong(String username);
       ```

       and the difference is subtle but important:

       ```java
       getUserEmail("john"); // SELECT userEmail FROM Users WHERE userName="john";
       getUserEmailWrong("john"); // SELECT userEmail FROM Users WHERE userName=john;
       ```

     - To **access fields of a variable** in the method argument, use `#{getterMethodName}` or `${getterMethodName}`

       *For Example:*

       ```java
       @Insert("INSERT INTO Users (userName, userEmail, userRole) values " +
                   "(#{userName}, #{userEmail}, #{userRole})")
       Integer createNewUser(User user);
       ```

       this basically queries:

       ```java
       User user = new User();
       user.setUserName("happy");
       user.setUserEmail("happy123@gmail.com");
       user.setUserRole("premium");
       
       createNewUser(user); // INSERT INTO Users (userName, userEmail, userRole) values ('happy', 'happy123@gmail.com', 'premium');
       ```

       > **Note:**
       >
       > - when you have multiple objects in the method argument, you need to specify a `@Param` annotation to help MyBatis differentiate them. For example:
       >
       >   ```mysql
       >   Integer multiObjects(@Param("user1") User user1, @Param("user2") User user2);
       >   ```
       >
       >   then access their values with:
       >
       >   ```java
       >   @Insert("... #{user1.someGetter}, #{user2.someGetter}")
       >   Integer multiObjects(@Param("user1") User user1, @Param("user2") User user2);
       >   ```

3. Add some of your own logic/checks before executing a query!

   - Go to the ` CRUDService.java` and ` CRUDServiceImpl.java` class, and encapsulate all your complicated logics/checking here.

     - A good practice in general is to use an `Interface` before implementing the methods.

   - In the `interface CRUDService`, specify the methods that you want it to provide.

     *For Example: To Simply Provide the above CRUD operation service:*

     First, specify in the interface what methods/functionalities you would like to have:

     ```java
     @Service // this annotation is optional
     public interface CRUDService {
         // here I used Object instead of the exact return type. 
         // In general, if you know what you are dealing with, either way is fine
         Object getAllUsers();
         Object getUserEmail(String userName);
         int createNewUser(User user);
         int updateUser(User user);
         int deleteUser(Integer userId);
     }
     ```

     Then, provide the implementation:

     ```java
     @Service // this annotation is necessary
     public class CRUDServiceImpl implements CRUDService{
         @Autowired
         private CRUDMapper mapper;
     
         @Override
         public Object getAllUsers() {
             // your logic/checking here
             return this.mapper.getAllUsers();
         }
     
         @Override
         public Object getUserEmail(String userName) {
             // your logic/checking here
             return this.mapper.getUserEmail(userName);
         }
     
         @Override
         public int createNewUser(User user) {
             // your logic/checking here
             return this.mapper.createNewUser(user);
         }
     
         @Override
         public int updateUser(User user) {
             // your logic/checking here
             return this.mapper.updateUser(user);
         }
     
         @Override
         public int deleteUser(Integer userId) {
             // your logic/checking here
             return this.mapper.deleteUser(userId);
         }
     }
     ```

     where:

     - `@Service` on the top tells Spring that this is a Service component. Therefore, Spring will **add this class into its injector**.

     - `@Autowired` is one of the most powerful functionality that Spring provides. It tells the injector to **inject** an instance of the class `CRUDMapper` into this the instance of `CRUDServiceImpl`.

       - But wait, `CRUDMapper` is an interface! In fact, this is where `MyBatis` does all the work of providing a working instance from the interface you wrote.

     - Now, all you need to is to use the `Mapper` and do the queries!

       *For Example:*

       ```java
       @Override
       public Object getUserEmail(String userName) {
           // instead of directly using the Mapper, 
           // you might want to check if the userName is in an expected format
           // for example, it does not contain symbols like +];/= and etc
           return this.mapper.getUserEmail(userName);
       }
       ```

       > **Extension:**
       >
       > - There is a neat way to provide those checks, and also make them reusable easily. To do this, you need to setup your own `Validator` annotation. If you are interested, please refer to [this link](https://www.baeldung.com/spring-mvc-custom-validator) for a quick guide.

4. Last but not least, you want to expose your query data to your frontend. 

   To do this, you should go to your `CRUDController` class, and setup:

   ```java
   @RestController // this annotation is necessary
   public class CRUDController {
       @Autowired // Spring looks for an implementation of the interface, and injects CRUDServiceImpl!
       private CRUDService service;
   
       /**
        * A Simple Example: Listens for the GET request with path '/test'
        * @return a String "DevFest 2021"
        */
       @GetMapping("/test")
       public String testAPI() {
           return "DevFest 2021";
       }
   
       @GetMapping("/getAllUsers")
       public Object getAllUsers() {
           return this.service.getAllUsers();
       }
   
       @GetMapping("/getUserEmail")
       public Object getUserEmail(@RequestParam String userName){
           return this.service.getUserEmail(userName);
       }
   
       @PostMapping("/createNewUser")
       public String createNewUser(User user){
           return this.service.createNewUser(user) == 1? "success":"failed";
       }
   
       @PostMapping("/updateUser")
       public String updateUser(User user){
           return this.service.updateUser(user) == 1? "success":"failed";
       }
   
       @DeleteMapping("/deleteUser")
       public String deleteUser(@RequestParam Integer userId){
           return this.service.deleteUser(userId) == 1? "success":"failed";
       }
   }
   ```

   where:

   - If you need to **access the request parameters** in the **GET request**, use `@RequestParam` before the method argument

     *For Example:*

     ```java
     @GetMapping("/getUserEmail")
     public Object getUserEmail(@RequestParam String userName){
         return this.service.getUserEmail(userName);
     }
     ```

     then a request like:

     ```html
     localhost:8080/getUserEmail?userName=john
     ```

     will be parsed into the method as:

     ```java
     public Object getUserEmail(String username="john"){
         return this.service.getUserEmail(userName);
     }
     ```

   - If you need to **access the request body** in the **POST request**, specify nothing.

     - You can try using the `@RequestBody` annotation. However, then you might need to configure the supported media type as well.

     *For Example:*

     ```java
     @PostMapping("/createNewUser")
     public String createNewUser(User user){
         return this.service.createNewUser(user) == 1? "success":"failed";
     }
     ```

     then a POST request like:

     ```bash
     curl --location --request POST 'localhost:8080/createNewUser' \
     --form 'userName="mike"' \
     --form 'userEmail="mike123@gmail.com"' \
     --form 'userRole="free"'
     ```

     would create a `User` with `userName="mike"`, `userEmail="mike123@gmail.com"`, `"userRole="free"` and pass into the controller as the `user` variable.

# Epilogue

***If you reached this point, congratulations, you have successfully setup a containerized `MySQL` database + a Spring backend connected to that database! The powerful aspect of this setup is that everything is portable! You can package your `Spring` application into the `jar` file with `mvn package`, and then setup your database with Docker anywhere you want.***

Unfortunately, due to limited time, I cannot cover all the powerful functionalities of using Docker and Spring (and Kubernetes) as backend service combo. However, if you have worked through and understood most of the contents in this tutorial, you should be good to go to setting up your own complicated backend service for your own projects!

- If you are interested in what more Docker and Spring can do, here are some techniques I use in my own projects:
  - setup `Redis` in docker and connect Spring to that as well
  - use `Spring Security` to deal with network security in the project, including adding filters/interceptors and validators
  - use `Spring` to setup an OAuth2.0 login
  - use `Spring Cloud` to setup load balancing for the backend server together with `Docker`+`Kubernetes`
  - etc.

***Last but not least, thank you for your time reading this tutorial/coming to the workshop! If you have any questions, feel free to reach me via email: xy2437@columbia.edu***