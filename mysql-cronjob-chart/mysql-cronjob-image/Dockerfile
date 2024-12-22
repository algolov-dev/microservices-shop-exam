FROM mysql:8.3.0

COPY get_databases.sh /app/get_databases.sh

RUN chmod +x /app/get_databases.sh

CMD ["/app/get_databases.sh"]