<#-- @ftlvariable name="printer" type="de.kammerchorwernigerode.telegrambot.tg2smtp.bot.PollPrinter" -->
<#assign poll = printer.print(model, .locale_object)>
Hello,

somebody created a poll. Ask your responsible if you want to participate in this poll.

${poll}
