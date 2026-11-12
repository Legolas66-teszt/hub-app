# Hub app (Android)

A Zigbee hub és a Matter-egység weboldala appként – ugyanaz az oldal, mint a böngészőben. Egy kódból két app készül:

- **Zigbee** (`hu.zigbeehub.app`, kék ikon) – a Zigbee hub; `hub-app.apk`
- **Matter** (`hu.zigbeehub.app.matter`, lila ikon) – a Matter-egység; `matter-app.apk`

A két app külön beállításokkal él (mindkettőben egyszer meg kell adni a saját egység otthoni címét), de a Tailscale-t
egyeztetik: amíg az egyik előtérben van, a másik nem kapcsolja ki alóla; a kikapcsolást a később háttérbe kerülő végzi.
A Matter-egység ugyanazt a webszervert futtatja (`/api/ping`, Tailscale), ezért az appon belül nincs különbség – csak a
név, az ikon színe és a gépnév (`app/src/matter/res`).

Amit az app hozzátesz:

- **Magától választ utat:** wifin előbb a hub otthoni címét próbálja (kb. 1 s). Ha a hub válaszol, otthon vagy:
  a Tailscale-t nem kapcsolja be (ha ő kapcsolta be korábban, kikapcsolja).
- **Távol:** bekapcsolja a Tailscale appot, megvárja, és a hub Tailscale-címét nyitja meg.
- **Akku:** háttérbe téve 2 perc múlva kikapcsolja a Tailscale-t – de csak ha ő kapcsolta be. A kézzel bekapcsolt
  Tailscale-hez nem nyúl.
- A hub Tailscale-címét az app otthon magától megtudja (`/api/ping`); csak az otthoni címet kell egyszer megadni.

Kell hozzá: a hubon az M22-es firmware (`/api/ping`), a telefonon a Tailscale app (bejelentkezve, egyszer már
engedélyezve a VPN-t).

## Fordítás (GitHub)

Minden feltöltés után a GitHub lefordítja mindkettőt (Actions → APK), a kész fájlok: **Releases → latest → hub-app.apk,
matter-app.apk**.

## Mi hol van

- `app/src/main/java/hu/zigbeehub/app/MainActivity.java` – a WebView, a hub megkeresése, fájlfeltöltés / mentés
- `Hub.java` – címek, `/api/ping`, a Tailscale be / ki (`com.tailscale.ipn.CONNECT_VPN` / `DISCONNECT_VPN`)
- `OffReceiver.java` – a Tailscale kikapcsolása 2 perccel a háttérbe kerülés után
- `SiblingReceiver.java` – a testvér-app (Zigbee ↔ Matter) jelzései, hogy ne kapcsolják ki egymás alól a Tailscale-t
- `app/src/matter/res` – a Matter-app eltérései (név, ikonszín, gépnév)
- `app/hub.keystore` – az app saját aláíró kulcsa (minden fordítás ezzel ír alá, így a frissítés a régi fölé települ)
