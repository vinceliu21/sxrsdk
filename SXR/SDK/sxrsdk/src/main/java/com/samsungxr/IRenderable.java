package com.samsungxr;

/**
 * Interface for a renderable entity.
 *
 * Something is renderable if it has an attached mesh and material.
 */
public interface IRenderable
{
    /**
     * Get the SXRMaterial associated with this renderable entity.
     * @return SXRMaterial
     */
    public SXRMaterial  getMaterial();

    /**
     * Get the SXRMesh associated with this renderable entity.
     * @return SXRMesh
     */
    public SXRMesh      getMesh();

    /**
     * Determine if this renderable uses light sources.
     */
    public boolean      isLightEnabled();

    /**
     * Save the native Shader ID for this renderable.
     * @param shaderID integer representing a native shader ID generated by SXRShader#bindShader
     */
    public void         setShader(int shaderID, boolean isMultiview);

    public SXRContext getSXRContext();
}
