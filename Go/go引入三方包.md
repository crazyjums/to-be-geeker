# 1 import当前目录下的package

目录结构：

```
/main.go
/dataGetClient.go
/mcommlib/MPair.go
```

main.go 和 dataGetClient.go 里面是package main

/mcommlib/MPair.go里面是package mcommlib

dataGetClient.go 会引用到 /mcommlib/MPair.go里面的内容，应该这么写：

```go
import (
	"encoding/binary"
	"errors"
 
	"./mcommlib"
)
```

在引用到时这样用：

```Go
pair := new(mcommlib.MPair)
```

前面加mcommlib