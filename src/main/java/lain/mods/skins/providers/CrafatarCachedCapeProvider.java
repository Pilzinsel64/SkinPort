package lain.mods.skins.providers;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Function;
import lain.mods.skins.api.interfaces.IPlayerProfile;
import lain.mods.skins.api.interfaces.ISkin;
import lain.mods.skins.api.interfaces.ISkinProvider;
import lain.mods.skins.impl.Shared;
import lain.mods.skins.impl.SkinData;
import lain.mods.skins.impl.forge.MinecraftUtils;

public class CrafatarCachedCapeProvider implements ISkinProvider
{

    private File _dirN;
    private File _dirU;
    private Function<ByteBuffer, ByteBuffer> _filter;

    public CrafatarCachedCapeProvider(Path workDir)
    {
        _dirN = new File(workDir.toFile(), "capes");
        _dirN.mkdirs();
        _dirU = new File(_dirN, "uuid");
        _dirU.mkdirs();

        for (File file : _dirN.listFiles())
            if (file.isFile())
                file.delete();
        for (File file : _dirU.listFiles())
            if (file.isFile())
                file.delete();
    }

    @Override
    public ISkin getSkin(IPlayerProfile profile)
    {
        SkinData skin = new SkinData();
        if (_filter != null)
            skin.setSkinFilter(_filter);
        Shared.pool.execute(() -> {
            byte[] data = null;
            UUID uuid = profile.getPlayerID();
            if (!Shared.isOfflinePlayer(profile.getPlayerID(), profile.getPlayerName()))
                data = CachedDownloader.create().setLocal(_dirU, uuid.toString()).setRemote("https://crafatar.com/capes/%s", uuid).setDataStore(Shared.store).setProxy(MinecraftUtils.getProxy()).setValidator(SkinData::validateData).read();
            if (data != null)
                skin.put(data, "cape");
        });
        return skin;
    }

    public CrafatarCachedCapeProvider withFilter(Function<ByteBuffer, ByteBuffer> filter)
    {
        _filter = filter;
        return this;
    }

}