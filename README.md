# Telegram to SMTP

A Telegram Bot that forwards chat massages to email addresses. Works in private chats, groups and channels.

## Use Case

Some of our club members refuse to join the club's private information broadcast Telegram channel. The organizer had to
write each message twice. One for Telegram and one for email distribution. This bot fixes the problem by simply sending
Telegram messages to a mailing list.


## Installation

### Requirements

* Java 11
* Docker Runtime (alternative)
* Docker Compose (optional)

### Instructions

1. Create a new Telegram Bot using the official Telegram [BotFather](https://core.telegram.org/bots#creating-a-new-bot)
   and note the Bot's username and HTTP API Token.
2. Invite your new Bot into the chat of your choice. Your Bot must have just enough permissions to read chat messages.
3. Retrieve the ID of the current chat.
   1. Send any message to the Telegram chat (or group or channel)
   2. Open https://q3qkk.csb.app/ and paste the Bot's token into the input field

The tool extracts the chat ID from the returned JSON response. Note down your chat ID.

Alternatively, if you don't want to use an untrusted tool, you can visit
`https://api.telegram.org/bot${token}/getUpdates` and replace `${token}` with your HTTP API Token. Find the number
where it reads `[...]"chat":{"id":>someNumber<` or
```json
{
  "ok": true,
  "result": [
    {
      [...],
      "message": {
        [...],
        "chat": {
          "id": >someNumber<, ← that is the number you want
          [...]
        },
        [...]
      }
    }
  ]
}
```

### Deployment

The Telegram Bot you have created is just a gateway between Telegram and the actual Service Tg2SMTP. You must start this
service in order to retrieve messages and send emails. You'll probably want to run Tg2SMTP on a server either way to
guarantee mails get sent instantly.

There are two ways:
1. The traditional and probably most common way is to start the executable `.jar`-file and
2. a more manageable method using Docker.

#### Configuration

Before you can start you should know your SMTP provider's hostname/server address/SMTP server, port and authentication
mechanisms and constraints as well as your login credentials. Most providers provide a FAQ for common email clients like
Outlook, Thunderbird etc.

The following table shows all available configuration properties. Choose either Property or Environment Variable
column depending on your deployment method. Spring, the underlying framework, allows a deployment method independent way
to provide configuration for Tg2SMTP. To do this, write the desired configuration key-value pairs in an
`application.properties` or `application.yml` file and place the file next to the `.jar` file. Spring then reads this
file at startup.

| Property                                     | Environment Variable   | Datatype   | Default Value        | Example Value                                         | Description                                                                  |
|----------------------------------------------|------------------------|------------|----------------------|-------------------------------------------------------|------------------------------------------------------------------------------|
| _tg2smtp.subject_                            | TG2SMTP_MAIL_SUBJECT   | _`String`_ |                      | `"[Telegram] New Message"`                            | The mail's subject line                                                      |
| _tg2smtp.to_                                 | TG2SMTP_MAIL_TO        | _`String`_ |                      | `john@example.com,jane@example.com`                   | Comma-separated list of zero or more mail recipients                         |
| _tg2smtp.chat-id_                            | TG2SMTP_CHAT_ID        | _`Number`_ |                      | `1337420,-0815`                                       | Comma-separated list of zero or more Telegram Chat IDs                       |
| _tg2smtp.bot.username_                       | TG2SMTP_BOT_USERNAME   | _`String`_ |                      | `MyAwesoneTg2SmtpBot`                                 | The Bot's username                                                           |
| _tg2smtp.bot.token_                          | TG2SMTP_BOT_TOKEN      | _`String`_ |                      | `XXXXXXXX:YYYYYYYYY`                                  | The Bot's HTTP API Token                                                     |
| _spring.mail.host_                           | MAIL_HOST              | _`String`_ | `localhost`          |                                                       | The SMTP server that is used to send mails                                   |
| _spring.mail.port_                           | MAIL_PORT              | _`Number`_ | `587`                |                                                       | The SMTP server's port                                                       |
| _spring.mail.username_                       | MAIL_USERNAME          | _`String`_ | `smtp@example.com`   |                                                       | The SMTP username for authentication (required if _smtp_auth_ is active)     |
| _spring.mail.password_                       | MAIL_PASSWORD          | _`String`_ | `d3v`                |                                                       | The SMTP password for authentication (required if _smtp_auth_ is active)     |
| _spring.mail.properties.mail.smtp.localhost_ | MAIL_HELO              | _`String`_ | `localhost`          |                                                       | The well known hostname Tg2SMTP is running on (optional[^1])                 |
| _spring.mail.properties.mail.from_           | MAIL_FROM              | _`String`_ | `smtp@example.com`   |                                                       | The email address mails are sent from (optional while _smtp_auth_ is active) |
| _spring.mail.properties.mail.mail.reply.to_  | MAIL_REPLY_TO          | _`String`_ |                      |                                                       | Email address the recipient should answer to (optional)                      |
| _spring.profiles.active_                     | SPRING_PROFILES_ACTIVE | _`String`_ | `smtp_auth,smtp_tls` | `smtp_auth`, `smtp_tls`, `smtp_auth,smtp_tls` or none | Comma-separated list of active Spring profiles (optional)                    |

Both profiles, smtp_auth and smtp_tls, are active per default, as it is the most common authentication mechanism
throughout.

[^1]: Depends on the provider if the HELO header is necessary, but might resolve issues anyway if set

##### Jar

Download the `.jar`-file of the latest release. Execute the following command in your console to start Tg2SMTP. Don't
forget to replace the sample configuration with your own.

```shell
java -jar tg2smtp-0.0.1-SNAPSHOT.jar \
 --tg2smtp.subject="[Telegram] New message" \
 --tg2smtp.to=john.doe@example.com,jane.doe@example.com \
 --tg2smtp.chat-id=1337420 \
 --tg2smtp.bot.username=MyAwesoneTg2SmtpBot \
 --tg2smtp.bot.token=XXXXXXXX:YYYYYYYYY \
 --spring.mail.host=smtp.example.com \
 --spring.mail.port=587 \
 --spring.mail.username=user@example.com \
 --spring.mail.password=s3cr3t \
 --spring.mail.properties.mail.smtp.localhost=my-host.example.com \
 --spring.mail.properties.mail.from=user@example.com \
 --spring.mail.properties.mail.mail.reply.to=no-reply@example.com \
 --spring.profiles.active=smtp_auth,smtp_tls
```

Pass `--spring.profiles.active=` if you want to deactivate SMTP authentication and SMTP transport TLS encryption.

The configuration arguments above are not necessary if a configuration file is in the same directory.

#### Docker

Replace the sample configuration and run the command below to start a Tg2SMTP Docker Container.

```shell
docker run -d \
 -e TG2SMTP_MAIL_SUBJECT="[Telegram] New message" \
 -e TG2SMTP_MAIL_TO=john.doe@example.com,jane.doe@example.com \
 -e TG2SMTP_CHAT_ID=1337420 \
 -e TG2SMTP_BOT_USERNAME=MyAwesoneTg2SmtpBot \
 -e TG2SMTP_BOT_TOKEN=XXXXXXXX:YYYYYYYYY \
 -e MAIL_HOST=smtp.example.com \
 -e MAIL_PORT=587 \
 -e MAIL_USERNAME=user@example.com \
 -e MAIL_PASSWORD=s3cr3t \
 -e MAIL_HELO=my-host.example.com \
 -e MAIL_FROM=user@example.com \
 -e MAIL_REPLY_TO=no-reply@example.com \
 -e SPRING_PROFILES_ACTIVE=smtp_auth,smtp_tls \
 kchwr/tg2smtp:latest
```

The `example/` directory contains a Docker Compose file that configures Tg2SMTP the same way as the example above.

Docker Swarm Mode users may configure their stack using Docker Secrets. Just append `_FILE` to the appropriate
environment variables: `MAIL_USER_FILE`, `MAIL_PASSWORD_FILE`, `TG2SMTP_MAIL_TO_FILE`, `TG2SMTP_CHAT_ID_FILE`,
`TG2SMTP_BOT_USERNAME_FILE`, `TG2SMTP_BOT_TOKEN_FILE`. Keep in mind that only one Tg2SMTP instance with the same
username can register at the Telegram Bot API concurrently.


## Development

### Requirements

* Java 11
* Maven 3.x
* Docker Runtime
* Docker Compose

### Configuration

This project already provides sane defaults to configure Tg2SMTP properly. You may override these values to match your
preferences. To do so, create an `application.[yml,properties]`-file in your project root and override the properties
you want to configure.

### Setup

It's also a good idea to download dependencies and build the executable `.jar`-artifact once before you start hacking.

```shell
mvn package
```

Start a development SMTP server on your host for easier development. Received messages are stored in
`data/received-emails/`.

```shell
docker-compose -f docker-compose.dev.yml up -d
```

To stop the stack use the command below.

```shell
docker-compose -f docker-compose.dev.yml down
```

### Usage

Run Tg2SMTP by calling the command below. Depending on your environment and preferred IDE you can start the main-class
or main-method directly from your IDE.

```shell
mvn spring-boot:run
```


## Licence

Apache License 2.0 - [Kammerchor Wernigerode e.V.](https://kammerchor-wernigerode.de) - Built with :heart:
