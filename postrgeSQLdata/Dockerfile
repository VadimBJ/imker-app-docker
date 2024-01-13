FROM postgres:16-alpine

ADD init.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_DB=Imker
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=root

CMD ["postgres"]
