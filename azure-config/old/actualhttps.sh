sudo dnf update -y && sudo dnf install nginx vim -y

# make sure ports 80 and 443 are open

sudo vim /etc/nginx/conf.d/team18integration4.westeurope.cloudapp.azure.com.conf
# paste this:

server {
    listen 80;
    server_name team18integration4.westeurope.cloudapp.azure.com www.team18integration4.westeurope.cloudapp.azure.com;

    location /.well-known/acme-challenge/ {
        root /var/www/html;
    }
}


sudo vim /etc/nginx/nginx.conf

# enter un http block this line: 
http { ## already existing
    server_names_hash_bucket_size 128;
    ...
}

# should be successful: 
sudo nginx -t

sudo mkdir -p /var/www/html
sudo chown $USER:$USER /var/www/html

sudo mkdir -p /var/lib/letsencrypt
sudo chown $USER:$USER /var/lib/letsencrypt

sudo mkdir -p /etc/letsencrypt
sudo chown $USER:$USER /etc/letsencrypt

#correctly
sudo dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo
sudo dnf install -y docker-ce docker-ce-cli containerd.io

systemctl enable docker
systemctl start docker

# stop for a sec
systemctl stop nginx


# maybe use duckdns (in infra for sure cause he will run the project locally so only duckdns with its api will work fine)
# get certificates
sudo docker run --rm -it \
-p 80:80 \
-v /etc/letsencrypt:/etc/letsencrypt \
certbot/certbot:latest certonly \
--standalone \
--preferred-challenges http \ 
--non-interactive \
--agree-tos \
--email anir@saddik.dev \
-d team18integration4.westeurope.cloudapp.azure.com

sudo systemctl restart nginx



---

# 1. Relabel the entire letsencrypt tree as cert_t
sudo chcon -R -t cert_t /etc/letsencrypt

# 2. Double‑check one file
ls -lZ /etc/letsencrypt/live/team18integration4.westeurope.cloudapp.azure.com/fullchain.pem

# 3. Restart Nginx
sudo systemctl restart nginx

# Should show nginx listening on 443
sudo ss -tulpn | grep :443

# Should return HTTP/2 200 over HTTPS
curl -Ik https://team18integration4.westeurope.cloudapp.azure.com

vim /etc/nginx/conf.d/team18integration4.westeurope.cloudapp.azure.com.conf

# Redirect all HTTP → HTTPS
server {
    listen 80;
    server_name team18integration4.westeurope.cloudapp.azure.com;
    return 301 https://$host$request_uri;
}

# HTTPS server
server {
    listen 443 ssl http2;
    server_name team18integration4.westeurope.cloudapp.azure.com;

    ssl_certificate     /etc/letsencrypt/live/team18integration4.westeurope.cloudapp.azure.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/team18integration4.westeurope.cloudapp.azure.com/privkey.pem;

    root /var/www/html;
    index index.html;
}

[root@vm-team18-integration4 team18]# cat /etc/nginx/conf.d/team18integration4.westeurope.cloudapp.azure.com.conf
# Redirect HTTP → HTTPS
server {
    listen 80;
    server_name team18integration4.westeurope.cloudapp.azure.com;
    return 301 https://$host$request_uri;
}

# HTTPS reverse‑proxy to Spring Boot
server {
    listen 443 ssl http2;
    server_name team18integration4.westeurope.cloudapp.azure.com;

    ssl_certificate     /etc/letsencrypt/live/team18integration4.westeurope.cloudapp.azure.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/team18integration4.westeurope.cloudapp.azure.com/privkey.pem;

    location / {
        proxy_pass         http://127.0.0.1:8080;
        proxy_http_version 1.1;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto https;
        proxy_set_header   Upgrade           $http_upgrade;
        proxy_set_header   Connection        "upgrade";  # Correct way
    }
}
[root@vm-team18-integration4 team18]# 




mkdir -p /var/www/html
touch /var/www/html/index.html
sudo tee /var/www/html/index.html > /dev/null <<EOF
<!DOCTYPE html>
<html>
  <head><title>Welcome</title></head>
  <body><h1>Hello, HTTPS!</h1></body>
</html>
EOF

sudo chown nginx:nginx /var/www/html/index.html
sudo chmod 644 /var/www/html/index.html

sudo restorecon -v /var/www/html/index.html
sudo systemctl restart nginx

sudo setsebool -P httpd_can_network_connect 1
delete html file


# should return 200 OK
[root@vm-team18-integration4 team18]# curl -Ik https://team18integration4.westeurope.cloudapp.azure.com

sudo dnf install -y java-21-openjdk
java -version
