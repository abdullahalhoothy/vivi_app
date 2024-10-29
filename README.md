# vivi_app


Created Dockerfile
Note:
Command to Create Apk through docker
-> docker build -t your-image-name .

Run the Container: First, you can run a temporary container to access the filesystem:
-> docker run --rm -it --name temp-container my-android-debug-build /bin/bash

To get container id
-> hostname

-> run below command in new terminal to Copy Apk to new location
docker cp :/app/app/build/outputs/apk/debug/app-debug.apk /Users/rashid/Desktop
