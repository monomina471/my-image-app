# Image Sharing App

## 概要
画像をアップロード・検索・削除できるWebアプリです。

## 使用技術
- Frontend: React, Vite
- Backend: Spring Boot
- DB: MySQL (RDS)
- Auth: JWT
- Infra: AWS (EB, RDS, S3, CloudFront)

## 機能
- ユーザー登録 / ログイン
- 画像アップロード
- タグ検索
- 画像削除

## デプロイ構成
- Frontend: S3 + CloudFront
- Backend: Elastic Beanstalk
