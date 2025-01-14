/*
    Copyright(c) 2019 Risto Lahtela (AuroraLS3)

    The MIT License(MIT)

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files(the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions :
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package com.djrapitops.extension;

import com.djrapitops.plan.settings.ListenerService;
import com.djrapitops.plan.settings.SchedulerService;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.viaversion.viaversion.api.ViaAPI;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ViaVelocityVersionListener implements ViaListener {

    private final ViaAPI viaAPI;
    private final ViaVersionStorage storage;

    ViaVelocityVersionListener(
            ViaAPI viaAPI,
            ViaVersionStorage storage
    ) {
        this.viaAPI = viaAPI;
        this.storage = storage;
    }

    @Override
    public void register() {
        ListenerService.getInstance().registerListenerForPlan(this);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onPostLogin(PostLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        int playerVersion = viaAPI.getPlayerVersion(uuid);

        SchedulerService.getInstance().runAsync(() -> {
            try {
                storage.storeProtocolVersion(uuid, playerVersion);
            } catch (ExecutionException ignored) {
                // Ignore
            }
        });
    }
}
