# Image Management App

## 概要
画像をアップロード・検索・削除できるWebアプリです。このアプリは画像をタグで管理・検索し、共通のタグを持つ関連画像も一緒に探すことのできる画像管理アプリです。ローカルで保存している画像をより手軽に管理し、探したいときにすぐ見つけられることを目的に作成しました。
Spring Boot + Reactでアプリを制作し、AWSを利用して独自ドメインでアプリを公開しています。

## URL
https://www.minaminachan.com/


## アプリ画面のサンプル画像
<img width="1394" height="1392" alt="image" src="https://github.com/user-attachments/assets/4d0b855a-fcd8-4604-a9c4-ce35071b1228" />

<img width="1397" height="1392" alt="image" src="https://github.com/user-attachments/assets/16420d4f-a9cc-4322-ac97-71268802b36d" />

<img width="1397" height="1392" alt="image" src="https://github.com/user-attachments/assets/27b39355-b070-4f17-91f4-ce1318fd77a9" />


## 実装した機能
- ユーザー登録・ログイン（JWT認証を使用）
- 画像アップロード（S3へ保存）
- タグ付与・検索機能
- 自分がアップロードした画像一覧表示

## 苦労した点

### 画像をPOSTメソッドで送信する処理の実装
画像を送信する際のContent-Typeの設定に苦労しました。文字列の場合はapplication/jsonを使えば解決しますが、このアプリの場合は画像ファイル・タグ。ユーザーIDを同時に送信する必要があるため、どのようなContent-Typeを使用するのが良いか、非常に悩みました。
→ ネット記事やAIを使って調べた結果、FormDataオブジェクトに送信する情報を詰め込み、multipart/form-data形式でバックエンドに送信して@RequestParamでデータを取り出すという形に落ち着きました。

参考にした記事 : https://qiita.com/natuuu0831/items/8b392ad47133b575b620

### Elactic Beanstalkの環境にロードバランサ―を導入し、ACMの証明書を適用させるまでの流れ
単一インスタンスで動かしていた際にはうまく行っていたにも関わらず、ロードバランサ―を導入したとたんエラーが発生し環境のヘルスがSevereになってしまいました。
→ ログの中で"Caused by"から始まる行を探し、どの部分でエラーが起きているかを探るとCould not resolve placeholder 'aws.s3.bucket-name' in value "${aws.s3.bucket-name}"という一文が見つかり、自分がapplication.ymlで定義しているキーと他のファイルで呼び出しているキーが一致していないのが原因であると分かりました。結果として、エラーが起きた際にはまず自分の書いたコードに誤字脱字がある可能性を疑う必要があると痛感しました。


## 使用技術

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

