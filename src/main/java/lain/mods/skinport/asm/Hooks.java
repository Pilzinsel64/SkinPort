package lain.mods.skinport.asm;

import lain.mods.skinport.SkinPort;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class Hooks
{

    public static Render getEntityRenderObject(RenderManager manager, Entity entity, Render value)
    {
        return SkinPort.getEntityRenderObject(manager, entity, value);
    }

    public static void postRenderManagerInit(RenderManager manager)
    {
        SkinPort.postRenderManagerInit(manager);
    }

}