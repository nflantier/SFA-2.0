package noelflantier.sfartifacts.client.render;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.BlockInductor;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumTypeTech;
import noelflantier.sfartifacts.common.tileentities.TileInductor;

public class RenderInductor extends TileEntitySpecialRenderer<TileInductor>{
	private IModel model;
    private IBakedModel bakedModel;
	private Map<EnumTypeTech,IBakedModel> mapBakedModel= new HashMap<EnumTypeTech,IBakedModel>();

    private IBakedModel getBakedModel() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedModel == null) {
            try {
                model = ModelLoaderRegistry.getModel(new ResourceLocation(Ressources.MODID, "block/inductor-base.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedModel;
    }

    @Override
    public void renderTileEntityAt(TileInductor te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        //GlStateManager.disableRescaleNormal();

        // Render the rotating handles
        //renderHandles(te);
        
        //renderMultiModel(te);
        
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

    private void renderMultiModel(TileInductor te) {
		
	}

	private void renderHandles(TileInductor te) {
        GlStateManager.pushMatrix();
        //System.out.println(te.facing.getOpposite().getHorizontalAngle());
        //GlStateManager.pushMatrix();
        //GlStateManager.translate(0.5, 0.5, 0);
        long angle = (System.currentTimeMillis() / 10) % 360;
        //GlStateManager.rotate(90, 1, 0, 0);
        //GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = te.getWorld();
        // Translate back to local view coordinates so that we can do the acual rendering here
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        IBlockState st = te.getWorld().getBlockState(te.getPos());
        IBlockState ast = st.getBlock().getActualState(st, te.getWorld(),te.getPos());
        IExtendedBlockState east = (IExtendedBlockState)st.getBlock().getExtendedState(st, te.getWorld(), te.getPos());
        EnumTypeTech tt = ast.getValue(BlockInductor.TYPETECH);
        if(te.facing.equals(EnumFacing.DOWN)){
        }else if(te.facing.equals(EnumFacing.UP)){
            //GlStateManager.translate(0, -0.5, -0.5);
            //GlStateManager.translate(0.5, 0.5, 0);
        	//GlStateManager.rotate(1, 0, 0, 1);
            //GlStateManager.translate(-0.5, -0.5, 0);
        	//GlStateManager.translate(-0.5, -0.5, 0);
			//GL11.glTranslatef(0F, -1F, 0F);
        }else if(te.facing.equals(EnumFacing.NORTH)){
			/*GL11.glRotatef(90, 1F, 0F, 0F);
			GL11.glTranslatef(0F, -0.5F, -0.5F);*/
        }else if(te.facing.equals(EnumFacing.SOUTH)){
			/*GL11.glRotatef(-90, 1F, 0F, 0F);
			GL11.glTranslatef(0F, -0.5F, +0.5F);*/
        }else if(te.facing.equals(EnumFacing.EAST)){
			/*GL11.glRotatef(-90, 0F, 0F, 1F);
			GL11.glTranslatef(-0.5F, -0.5F, 0F);*/
        }else if(te.facing.equals(EnumFacing.WEST)){
			/*GL11.glRotatef(90, 0F, 0F, 1F);
			GL11.glTranslatef(+0.5F, -0.5F, 0F);*/
        }
        //ds
        //if(!mapBakedModel.containsValue(tt))
        	//mapBakedModel.put(tt,bakedModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(ast));
        //mapBakedModel.get(tt)
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                world,
                getBakedModel(),
                world.getBlockState(te.getPos()),
                te.getPos(),
                Tessellator.getInstance().getBuffer(),
                false);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
    
	/*public IBakedModel theModel;
	
	public RenderInductor(){
		
	}*/

	/*public void addVertex(List<BakedQuad> listquad, WorldRenderer worldrenderer, int addMode){
		if(addMode==1){
			listquad.forEach((b)->worldrenderer.addVertexData(b.getVertexData()));
		}else if(addMode==2){
			for(BakedQuad bq :listquad){
    			//LightUtil.renderQuadColor(worldrenderer, bq, 0);
				int[] t = bq.getVertexData();
				for(int i = 0;i<t.length;i=i+7){
					worldrenderer.pos((double)Float.intBitsToFloat(t[i]),(double)Float.intBitsToFloat(t[i+1]),(double)Float.intBitsToFloat(t[i+2]))
					.tex((double)Float.intBitsToFloat(t[i+4]), (double)Float.intBitsToFloat(t[i+5])).endVertex();
				}
			}
		}
	}
	
	public void addIFlexibleBakedModelVertex(IFlexibleBakedModel m, WorldRenderer worldrenderer, int addMode){
		addVertex(m.getGeneralQuads(),worldrenderer,addMode);
        //for (EnumFacing face : EnumFacing.values()){
        //	addVertex(m.getFaceQuads(face),worldrenderer,addMode);
        //}
	}
	
	public void handlePart(String name, IFlexibleBakedModel m, WorldRenderer worldrenderer, Tessellator tessellator, IBlockState state, VertexFormat vformat){
		if(name.equals("staff")){
        	//int frX = Math.abs(state.getValue(BlockInductor.ALL_FACING).getFrontOffsetX());
        	//int frY = Math.abs(state.getValue(BlockInductor.ALL_FACING).getFrontOffsetY());
        	//int frZ = Math.abs(state.getValue(BlockInductor.ALL_FACING).getFrontOffsetZ());
	        GlStateManager.pushMatrix();
	        	//GlStateManager.translate(frX>0?0:0.5F,frY>0?0:0.5F,frZ>0?0:0.5F);
	        	worldrenderer.begin(7, Attributes.DEFAULT_BAKED_FORMAT);
	        	//float rot = ModEvents.instance.getClientTick()%(360/4)*4;
	        	//GlStateManager.rotate(rot, frX, frY, frZ);
	        	//GlStateManager.translate(frX>0?0:-0.5F,frY>0?0:-0.5F,frZ>0?0:-0.5F);
	        	addIFlexibleBakedModelVertex(m, worldrenderer, 1);
			    tessellator.draw();
	        GlStateManager.popMatrix();
		}else if(name.equals("base")){
	        worldrenderer.begin(7, Attributes.DEFAULT_BAKED_FORMAT );//DefaultVertexFormats.POSITION_TEX  Attributes.DEFAULT_BAKED_FORMAT
	        addIFlexibleBakedModelVertex(m, worldrenderer, 1);
        	tessellator.draw();
		}
	}*/
	
	/*@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
    		GlStateManager.translate(x,y,z);
    		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		    Tessellator tessellator = Tessellator.getInstance();
	        BlockPos pos = te.getPos();
	        IBlockAccess world = MinecraftForgeClient.getRegionRenderCache(te.getWorld(), pos);
	        IBlockState state = world.getBlockState(pos);
		    theModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
		    System.out.println(theModel);
	        if(theModel!=null && theModel instanceof IBakedModel){
	        	IBakedModel min = (IBakedModel)theModel;
	        	for (EnumFacing face : EnumFacing.values()){
		        	//min.getQuads(state, face, (long)partialTicks).forEach((b)->worldrenderer.addVertexData(b.getVertexData()));;
		        	//addVertex(m.getFaceQuads(face),worldrenderer,addMode);
	            }
	        	GlStateManager.disableLighting();
		        //MultiModel.Baked min = (MultiModel.Baked)theModel;
	        	//handlePart("base",min.getBaseModel(), tessellator,state,min.getFormat());
	        	//min.getParts().forEach((s,m)->handlePart(s,m, worldrenderer,tessellator,state,min.getFormat()));
	        	
			}
    	GlStateManager.popMatrix();
	}*/

}
