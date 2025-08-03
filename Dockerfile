FROM ubuntu:latest
LABEL authors="gitol"

ENTRYPOINT ["top", "-b"]