# kStub

A simple kTor application to allow stubbing for mobile applications.


## Usage

    kStub> ./bin/kStub
    
    
### Structure

    kStub>
        - bin
            - kStub
        - libs (jar file dependencies)
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

`bodFile` - optional, but has higher priority than `body`

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
      
      

### File



## Todo
 