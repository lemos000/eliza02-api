FROM ubuntu:latest
LABEL authors="lemos"

ENTRYPOINT ["top", "-b"]