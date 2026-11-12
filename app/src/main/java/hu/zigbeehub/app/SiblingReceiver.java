package hu.zigbeehub.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * A testvér-app (Zigbee hub ↔ Matter-egység) jelzései. Csak az azonos kulccsal aláírt app küldheti
 * (hu.zigbeehub.permission.SIBLING, signature).
 *
 * - SIB_ACTIVE: a másik app előtérbe került – a mi Tailscale-kikapcsolásunk vár (a Tailscale-t ő használja).
 * - SIB_INACTIVE: a másik app háttérbe került – ha mi sem vagyunk előtérben, a szokásos 2 perc múlva mi is
 *   kikapcsolhatjuk (a másik app is ezt teszi; amelyik előbb jön, az kapcsolja ki, a második már nem talál VPN-t).
 */
public class SiblingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
        String a = i.getAction();
        if (Hub.A_ACTIVE.equals(a)) {
            Hub.setSiblingActive(c, true);
            OffReceiver.cancel(c);
        } else if (Hub.A_INACTIVE.equals(a)) {
            Hub.setSiblingActive(c, false);
            if (!Hub.fg) {
                OffReceiver.schedule(c); /* (csak ha a miénk: ha mi kapcsoltuk be, vagy csak a hubhoz kell) */
            }
        }
    }
}
