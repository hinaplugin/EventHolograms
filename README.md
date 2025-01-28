# EventHolograms
DiscordのイベントをMinecraftのホログラムに表示する

## イベントの登録
1. Discordにイベントを登録
2. MinecraftにEventHolograms・PlaceholderAPIを導入
3. config.ymlに必要項目を登録
4. サーバーを再起動
5. ホログラムのプラグインで%events_list%と設定

## Config.yml
```
token: "" //Discord Botのトークン
server-id: "" //イベントを取得するDiscordサーバーのID
filter: [] //表示するイベントを絞り込む
title: "" //ホログラムの1行目に表示するタイトル
footer: "" //ホログラムの最終行に表示するフッター
```

## フィルター
config.ymlのfilter設定では，以下のように設定できます．  
```
filter: "マイクラ"
```
イベント名にマイクラが含まれるイベントのみ取得
