<#-- @ftlvariable name="printer" type="de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService" -->
<#assign poll = printer.print(root.model, .locale_object)>
Someone created a poll: ${poll}

Ask a responsible person if you want to participate in this poll or want to know the result.
