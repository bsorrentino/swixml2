## Description ##

Below we describe the how to use the various borders available in swixml

### EmptyBorder ###

#### Syntax ####

`EmptyBorder[(top,left,bottom,right)]`

#### Example ####
```
border="EmptyBorder"
border="EmptyBorder(2,2,5,5)"
```

### TitledBorder ###

#### Syntax ####

`TitledBorder(title[,titleJustification[,titlePosition[,font]]])`

#### Example ####
```
border="TitledBorder(Simple)"
border="TitledBorder(Simple,CENTER)"
border="TitledBorder(Simple,RIGHT)"
border="TitledBorder(Simple,CENTER,BOTTOM)"
border="TitledBorder(Simple,CENTER,BOTTOM,Arial-BOLD-10)"
```

### LineBorder ###

#### Syntax ####

`LineBorder[(color[,thickness])]`

#### Example ####
```
border="LineBorder"
border="LineBorder(red)"
border="LineBorder(green,3)"
```


### EtchedBorder ###

#### Syntax ####

`EtchedBorder[()]`

#### Example ####
```
border="EtchedBorder"
```

### LoweredBevelBorder ###

#### Syntax ####

`LoweredBevelBorder[()]`

#### Example ####
```
border="LoweredBevelBorder"
```

### RaisedBevelBorder ###

#### Syntax ####

`RaisedBevelBorder[()]`

#### Example ####
```
border="RaisedBevelBorder"
```

### CompoundBorder ###

#### Syntax ####

`CompoundBorder(border1, border2)`

#### Example ####
```
border="CompoundBorder(TitledBorder(Note), EmptyBorder(4,4,2,2))"
```