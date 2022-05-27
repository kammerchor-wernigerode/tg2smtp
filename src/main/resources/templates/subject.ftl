<#-- @ftlvariable name="printer" type="de.kammerchorwernigerode.telegrambot.tg2smtp.print.app.PrinterService" -->
<#assign subject = printer.print(root.metadata, .locale_object)>
${subject}
