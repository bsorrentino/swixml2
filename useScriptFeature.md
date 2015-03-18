# Introduction #

From release 2.6 swiwml2 has included scripting features (JSR223)


# Example #


```

<?xml version="1.0" encoding="UTF-8" ?>

<dialog 
xmlns="http://www.swixml.org/2007/Swixml" 
xmlns:script="http://www.swixml.org/2007/Swixml/script"
	resizable="true" 
	title="Example Script usage" 
	undecorated="false" 
	alwaysOnTop="true"
	defaultCloseOperation="JFrame.HIDE_ON_CLOSE" 
	modal="false"
	>

<!-- 
JAVA SCRIPT THAT DEFINE 'CLOSE' ACTION 

client ==> Rendered object 
application ==> Application' rendering object
-->
<script>

function close(e) {

	client.setVisible(false);
	
	application.exit();
}

</script>
<panel layout="borderLayout">

<vbox constraints="BorderLayout.CENTER">

<!-- CLASSIC ACTION BINDING -->
<Button name="tb" id="toggleButton" text="action" action="onCLick"  />

<box.vstrut height="10"></box.vstrut>

</vbox>

<hbox constraints="BorderLayout.SOUTH">
<!-- SCRIPT ACTION BINDING -->
<button text="close" script:action="close"/>
</hbox>
</panel>


</dialog>


```