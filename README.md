# kStub

A simple [kTor](https://ktor.io/) application to allow stubbing for mobile applications.


## Usage

    kStub> ./bin/kStub
    

### Android

Android emulator can point to the local host machine by using 
`http://10.0.2.2/` in the application base path
    
### iOS

iOS simulators can access localhost as they are on the same network as host machine
    
### Structure

    kStub>
        - bin
            - kStub
        - libs (jar file dependencies)
        - config/kstub_config.json - User config
        - stub (user specs/body)
    

The spec/body files can be in any directory structure. 
Stub directory will be scanned to hunt for stubs.

#### Example:

Login route spec: `(stub/login/login.json)`
    
    {
        "request": {
            "method": "GET",
            "url": "/login"
        },
        "response": {
            "status": 200,
            "body": "{ }",
            "bodyFile": "login/login_body.json",
            "headers": {
                "Content-Type": "application/json"
            }
        }
    }
    
`body` field will be used if no bodyFile exists    

<br/>

Login route body: `(stub/login/login_body.json)`

    {
        "id": 12345,
        "publicId" : "123456",
        "username" : "manoj",
        "lastLogin": "20200505T140000"
    } 
    
    
## Routes

Keep all specs and body inside the stub folder on base path

### Spec

Specify a spec containing `request` and `response` details

### Body

#### Content 

For simple content this field can be used

`body` - mandatory
    
    example:
    {
        ...
        "body": "{ \"success\": true }",
        ...
    }

#### File

`bodyFile` - optional, but has higher priority than `body`

    example:
    {
        ...
        "bodyFile": "login/login_body.json",
        ...
    }

This file can contain anything

## Configuration

### Command Line

      --port=8090
      
Command line arguments override file arguments for `port` etc, if duplicate. 

### File

`config/kstub_config.json` of this format:
    
    {
        "port": 8888
    }

| Property | Detail | Default |
|:--------:|:------:|:-------:|
|`port`    |Port    |8080|

## Todo

* Other Files for body
* Content Type other than JSON
* Admin portal to manage on the fly
* Allow editing spec/body in realtime, for subsequent requests
 