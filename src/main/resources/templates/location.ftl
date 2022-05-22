<#-- @ftlvariable name="printer" type="de.kammerchorwernigerode.telegrambot.tg2smtp.bot.LocationPrinter" -->
<#assign location = printer.print(model, .locale_object)>
Hello,

somebody shared a location:

* ${location}
