# Image Sharing App

## 概要
画像をアップロード・検索・削除できるWebアプリです。このアプリは画像をタグで管理・検索し、共通のタグを持つ関連画像も一緒に探すことのできる画像管理アプリです。Spring Boot + Reactでアプリを制作し、AWSを利用して独自ドメインでアプリを公開しています。

## URL
https://www.minaminachan.com/

<img width="1394" height="1392" alt="image" src="https://github.com/user-attachments/assets/4d0b855a-fcd8-4604-a9c4-ce35071b1228" />

<img width="1397" height="1392" alt="image" src="https://github.com/user-attachments/assets/16420d4f-a9cc-4322-ac97-71268802b36d" />

<img width="1397" height="1392" alt="image" src="https://github.com/user-attachments/assets/27b39355-b070-4f17-91f4-ce1318fd77a9" />


### フロントエンド
- React
- Vite
- JavaScript
- HTML / CSS

### バックエンド
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- REST API

### インフラ / クラウド
<img width="1681" height="1161" alt="インフラ構成図 drawio" src="https://github.com/user-attachments/assets/1bac01fe-7e3b-4b9c-853b-611f0ff577b5" />

- AWS
  - S3
  - CloudFront
  - Route 53
  - ACM
  - Elastic Beanstalk
  - EC2
  - ALB
  - RDS（MySQL）
  - Secrets Manager
  - VPC / VPC Endpoint

### データベース
- MySQL（Amazon RDS）

### 開発環境・その他
- Docker
- Git / GitHub
