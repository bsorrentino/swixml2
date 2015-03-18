## Introduction ##

The swixml2 provides a possibility to implement async action execution. This is achieved returning a Task instance upon action execution (see example below )

```
	@Action(enabledProperty = POSSIBLE_RUN_SCRIPT, block = BlockingScope.APPLICATION)
	public Task<?, ?> runScript() {
        }

```

If we define the **block** attribute,  automatically swixml will show a predefined 'progress dialog box', named **BlockingDialog** ,  until the task completion.

#### Blocking Dialog Configuration ####

> Blocking Dialog components are initialized from the following Task resources:

```
    BlockingDialog.title
    BlockingDialog.optionPane.icon
    BlockingDialog.optionPane.message
    BlockingDialog.cancelButton.text
    BlockingDialog.cancelButton.icon
    BlockingDialog.progressBar.stringPainted
```

> If the Task has an Action then use the actionName as a prefix

```
    actionName.BlockingDialog.title
    actionName.BlockingDialog.optionPane.icon
    actionName.BlockingDialog.optionPane.message
    actionName.BlockingDialog.cancelButton.text
    actionName.BlockingDialog.cancelButton.icon
    actionName.BlockingDialog.progressBar.stringPainted
```