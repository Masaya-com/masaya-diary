self.addEventListener('push', (event) => {
  event.waitUntil((async () => {
    let data = {};
    try {
      if (event.data) {
        // JSON優先、ダメならtext→JSON再チャレンジ
        try {
          data = event.data.json();
        } catch {
          const t = await event.data.text();
          try { data = JSON.parse(t); } catch { data = { body: t }; }
        }
      }
    } catch (e) { /* noop */ }

    const title = data.title || '本日のタスク';
    const body  = data.body  || 'タスクを確認しましょう！';

    // SWスコープ基準でパスを解決（/masaya-diary 配下でも安全）
    const icon  = new URL(data.icon  || '/icons/icon.png', self.registration.scope).toString();
    const badge = new URL(data.badge || '/icons/badge.png',    self.registration.scope).toString();

    // クリック先をpayloadで可変に（なければ既定）
    const url   = data.url || new URL('/masaya-diary/diary/top', self.registration.scope).toString();

    await self.registration.showNotification(title, {
      body,
      icon,
      badge,
      data: { url },               // ← クリック時に使う
      requireInteraction: false,   // 任意: 自動的に消えてOKなら false
      // tag: 'daily-task',        // 任意: 同一タグで上書きしたいとき
      // renotify: true            // 任意: 上書き時に再通知音
    });
  })());
});

self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  const targetUrl = event.notification?.data?.url ||
                    new URL('/masaya-diary/diary/top', self.registration.scope).toString();

  event.waitUntil((async () => {
    const allClients = await clients.matchAll({ type: 'window', includeUncontrolled: true });
    // 既存タブで同一オリジンのものがあればフォーカス＆遷移
    for (const client of allClients) {
      // すでに同じページ or 同一オリジンならそれを活用
      try {
        const clientUrl = new URL(client.url);
        const target    = new URL(targetUrl);
        if (clientUrl.origin === target.origin) {
          await client.focus();
          // SPAならここで postMessage してルーター遷移でもOK
          if ('navigate' in client) {
            await client.navigate(targetUrl);
          } else {
            // navigate未対応ブラウザ対策
            await clients.openWindow(targetUrl);
          }
          return;
        }
      } catch { /* noop */ }
    }
    // 既存タブがなければ新規で開く
    await clients.openWindow(targetUrl);
  })());
});
