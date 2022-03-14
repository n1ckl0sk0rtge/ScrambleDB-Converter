# Converter (with Unlinkable Pseudonyms)

This project contains the code for a so-called converter, which is an application for generation and converting unlinkable pseudonyms.
It is part of the ScrambleDB setup and provides the database driver with a rest interface for unlinkable pseudonyms.

## Install and run the application

The converter is build with quarkus. The raw application can be started from source code by executing the pre-defined gradle task. 
```shell script
./gradlew quarkusDev
```
The second way to run the converter application is by building and starting a Docker image. 
The root directory of this project contains a predefined Dockerfile for building an executable Docker image.
```shell script
docker build -t converter:latest .
```

## Using the Converter

The application exposes an api with two endpoints. This is always the case and by that it is the default way to communicate to the converter.

| Endpoint           | Description                                                                                                                                         |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| [/api/pseudonym]() | by accessing this endpoint the convert will generate new pseudonyms for the given input.                                                            |
| [/api/convert]()   | the converter will convert the given pseudonyms in so-called identities, which have the property to be identical for the same input (NOT pseudonym) | 

### Generate Pseudonyms

#### Sample request
```shell script
curl --location --request POST 'http://localhost:8080/api/pseudonym' \
--header 'Content-Type: application/json' \
--data-raw '{
    "data": [
        "inputValue1",
        "inputValue1"
        "inputValue2"
    ]
}'
```
#### Sample response
```json
[
    "OiDYqc6S3AjuppmI3B161kmGI6tnuWbPkYsAVWZD3Doz6wbMqkRgsyGJhkPjzYDG8phqjVzEup0ClkoLMcPMWcR2E6TGBw//hoItr29nU6JJNvDOTS9LjVvoKZXtqu6l4gntJngNMWwNUfjxggAl2XOvj4N7cFsq1ZGDoP7+mNovmo5oQ9wDSwCZ+x1jD5ychhJRCw432l98pLHVT6ATq60tHyET8lUh0BH4E0Soq0qkQMPh5YuqY737hByFUTfTLUYZRwH61E2DTN/Iah9wFEJzpcOtFG0S7jTbYlKdMIUxsydjII996puDGUa2vxzmM2LQHw73zQvStr3nG4TTpQ==",
    "NuvG5zoM1tMldPVsEYCrexrNSZWTzSX1dOrdcf9kVmNGMvk+ahXq/Z4mLF21gkfP+Lq1+s90yoZEy3W9d4CIO6lBabD4U7iBQgM7frvKuMC4iwz1Bm6hWmcyChb93I6N9yU2MmttLqSALbrzl6mi27fmtmrQXPmj4xm/uGtHrBxgkor0doQ7zsSlfB5ZM0p5Ru9DlGTTcx5/Ab8jffLgftwOA7Ft3wsy0ou/6iuVlLEdmE9o7J9We0RJlXOxDN1QXwkW6vchKD2z6NjtVur/9sLgLjz7TD4rA2YzsZKKIkckqnG2OohKAeyTCprUOdvWsUvXc5rgSQ6UeMwA/FfR3g==",
    "NEp2rypv2wmjRMHrHkyXAAr2I1MujjnAcfYkUCoTDZlUmc6zAPDWA6CRGVHAtVrc6g3KjYItUJ2dnFzXbIOTFETOSd9ELtxd41xytvIDDTnCPkQJXTsPTLgTQVmXoP2mW/zMt+ee7oMpbfQCq2a4TVTADeZg55qC28PrNgCCZsEFCwCe4IKZI2UX8AtQlhK3tWXQhLdOoxVYAwO9Rcd2FV8acM1tNqwYEmFLMua0gQfYc36rGHAJ8rgCUFs9lSj/HWwD4z2bg+hkqZT2tQRCDrLHc2wsfF5TFjOYWUUCHr0F+dMYMOqLnVit7JEcNWTUD/9ZI6jLzXGAoVaMtp7DkA=="
]
```

### Convert Pseudonyms

#### Sample request
```shell script
curl --location --request POST 'http://localhost:8080/api/convert/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "data": [
        "OiDYqc6S3AjuppmI3B161kmGI6tnuWbPkYsAVWZD3Doz6wbMqkRgsyGJhkPjzYDG8phqjVzEup0ClkoLMcPMWcR2E6TGBw//hoItr29nU6JJNvDOTS9LjVvoKZXtqu6l4gntJngNMWwNUfjxggAl2XOvj4N7cFsq1ZGDoP7+mNovmo5oQ9wDSwCZ+x1jD5ychhJRCw432l98pLHVT6ATq60tHyET8lUh0BH4E0Soq0qkQMPh5YuqY737hByFUTfTLUYZRwH61E2DTN/Iah9wFEJzpcOtFG0S7jTbYlKdMIUxsydjII996puDGUa2vxzmM2LQHw73zQvStr3nG4TTpQ==",
        "NuvG5zoM1tMldPVsEYCrexrNSZWTzSX1dOrdcf9kVmNGMvk+ahXq/Z4mLF21gkfP+Lq1+s90yoZEy3W9d4CIO6lBabD4U7iBQgM7frvKuMC4iwz1Bm6hWmcyChb93I6N9yU2MmttLqSALbrzl6mi27fmtmrQXPmj4xm/uGtHrBxgkor0doQ7zsSlfB5ZM0p5Ru9DlGTTcx5/Ab8jffLgftwOA7Ft3wsy0ou/6iuVlLEdmE9o7J9We0RJlXOxDN1QXwkW6vchKD2z6NjtVur/9sLgLjz7TD4rA2YzsZKKIkckqnG2OohKAeyTCprUOdvWsUvXc5rgSQ6UeMwA/FfR3g==",
        "NEp2rypv2wmjRMHrHkyXAAr2I1MujjnAcfYkUCoTDZlUmc6zAPDWA6CRGVHAtVrc6g3KjYItUJ2dnFzXbIOTFETOSd9ELtxd41xytvIDDTnCPkQJXTsPTLgTQVmXoP2mW/zMt+ee7oMpbfQCq2a4TVTADeZg55qC28PrNgCCZsEFCwCe4IKZI2UX8AtQlhK3tWXQhLdOoxVYAwO9Rcd2FV8acM1tNqwYEmFLMua0gQfYc36rGHAJ8rgCUFs9lSj/HWwD4z2bg+hkqZT2tQRCDrLHc2wsfF5TFjOYWUUCHr0F+dMYMOqLnVit7JEcNWTUD/9ZI6jLzXGAoVaMtp7DkA=="
    ]
}'
```

#### Sample response
```json
[
  "ZPf9mzztUQvKTuPQ3H/PDSDZkFVjqGyPprA1Jd0pDKjO4WZUhCNRNcZuKMnjQJllKXRvn0ZUqb7hHyTKyid426c+iNqk/8tPUGW4D71uxFbiCQmoMmD/sqoG6RguTw/44aDAWVJeMQuXfdz1dc7lTocVtk4WmaEUSPxYfaL6AhHUZ4Uh/NMe1E0almIIDpMNlFkK3Z5Se7u36zPJcMsjajz7slA+Ure1mgqcLWdVGohf6ckdioHgSswlJm66h5YCuyemjD7+3tZgaLXnJWNxgBWyDoNWn/Ewn3RF0RSnK/xr+kuvZ3c0Gnf6CG1s1bIso0tLdIeClRtBbQy0hIzsPA==",
  "ZPf9mzztUQvKTuPQ3H/PDSDZkFVjqGyPprA1Jd0pDKjO4WZUhCNRNcZuKMnjQJllKXRvn0ZUqb7hHyTKyid426c+iNqk/8tPUGW4D71uxFbiCQmoMmD/sqoG6RguTw/44aDAWVJeMQuXfdz1dc7lTocVtk4WmaEUSPxYfaL6AhHUZ4Uh/NMe1E0almIIDpMNlFkK3Z5Se7u36zPJcMsjajz7slA+Ure1mgqcLWdVGohf6ckdioHgSswlJm66h5YCuyemjD7+3tZgaLXnJWNxgBWyDoNWn/Ewn3RF0RSnK/xr+kuvZ3c0Gnf6CG1s1bIso0tLdIeClRtBbQy0hIzsPA==",
  "FOAnZgYQYqpgitr3IngAfKBg2E7o0+VyPxNNq+22rS6KH0lSAORvk6v4zeZbFdybtUivdu/Ue83p2eUAsroEjbOW05zt5J5aFMd+oYOeeAEAkmXbv1dsO1CQORh23o0C2NKFp92O6w0Vd7PdmEiQyvWtU+AKz+pGYUODGPs73JmF0rR8LXTphtrclcptGEK11EAhspsm6LqPy0xY3UOkwntpa4sKUQnl7BXy/7jWgE6c/HB9+wuhWXQ1CPAbceJC1IToHqitLAa6US84hG+IUSAgVuxhpEIUsF463G/jk9+2zvUD/eJvNjAVRshwUyfpZ3fFul3V3r9gnpnyBEzm0Q=="
]
```

## Configuration

The converter allows the configuration of some more properties.

| Environment variable   | Description                                                                                                                                                                                                                                                                                                                                                                  |
|------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `REDIS_HOST`           | Redis is used as an external storage to save key value pairs of pseudonyms and the corresponding secret exponent. This repository can be secured normally with redis out as the raw key value pairs without the public private key pair will not harm the integrity of this application. The stored data will be used for conversion. The defualt value is `localhost:6379`. |
| `REDIS_PASSWORD`       | Password for the redis instance. The default value is `redis`.                                                                                                                                                                                                                                                                                                               |
| `RSA_KEY_SIZE`         | In this PoC version of the converter the public private key pair will be created automatically on the first start and stored in the `WORKDIR_PATH`. This in not secure, but for the scope of this implementation it is enough. To configure the key size of the generated rsa key set this environment variable. The default value is `2048`.                                |
| `SECRET_EXPONENT_SIZE` | This variable defines the size of the secret exponents for the unlinkable pseudonyms. The default value is `256`.                                                                                                                                                                                                                                                            |
| `WORKDIR_PATH`         | Defines the path of the work dir. It will be used to store the key pair. Teh default value is `/data`.                                                                                                                                                                                                                                                                       |
