# vivi_app


Created Dockerfile
Note:
Command to Create Apk through docker

Dockerfile
Change the branch and Url in Dockerfile from to Clone the specific branch from GitHub
RUN git clone -b <branch_name> <repo_url> /root/build
-> RUN git clone -b main https://github.com/rashid1428/vivi_app.git /root/build

Terminal
-> docker build -t your-image-name .

Run the Container: First, you can run a temporary container to access the filesystem:
-> docker run --rm -it --name temp-container your-image-name /bin/bash

To get container id
-> hostname

run below command in new terminal to Copy Apk to new location
docker cp <Container_Id_here>:<Path_to_apk_file_in_container> <Destination_Path>
-> docker cp <Container_Id_here>:/root/build/app/build/outputs/apk/debug/app-debug.apk /Users/rashid/Desktop/Vivi_App
-> docker cp 67396085cd81:/root/build/app/build/outputs/apk/debug/app-debug.apk /Users/rashid/Desktop/Vivi_App
