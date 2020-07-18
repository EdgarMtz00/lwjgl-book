package com.edgarmtz.engine.graphics;

import com.edgarmtz.engine.utils.Resources;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to work with obj files
 */
public class ObjLoader {

    /**
     * Loads a obj file and converts it into a mesh representing the file's model
     * @param fileName Obj file
     * @return Mesh representing target file's model
     * @throws Exception If file doesn't exists or it's content can't be parsed
     */
    public static Mesh loadMesh(String fileName) throws Exception {
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals  = new ArrayList<>();
        List<Face> faces        = new ArrayList<>();

        List<String> fileLines = Resources.loadResourceLines(fileName);

        for (String line: fileLines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "v":
                    vertices.add(new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    ));
                    break;
                case "vt":
                    textures.add(new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    ));
                    break;
                case "vn":
                    normals.add(new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    ));
                    break;
                case "f":
                    faces.add(new Face(tokens[1], tokens[2], tokens[3]));
                    break;
                default:
                    break;
            }
        }
        return reorderLists(vertices, textures, normals, faces);
    }

    /**
     * Orders file read data so it can be converted into a valid model
     * @param verticesCoord Vertices position list
     * @param texturesCoord Texture position list
     * @param normalsCoord Normal vectors position list
     * @param faces List of faces formed in the model
     * @return Mesh made with properly related data
     */
    private static Mesh reorderLists(List<Vector3f> verticesCoord, List<Vector2f> texturesCoord, List<Vector3f> normalsCoord, List<Face> faces ) {
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray = new float[verticesCoord.size() * 3];
        int i = 0;
        for (Vector3f vertexCoord : verticesCoord) {
            verticesArray[i * 3] = vertexCoord.x;
            verticesArray[i * 3 + 1] = vertexCoord.y;
            verticesArray[i * 3 + 2] = vertexCoord.z;
            i++;
        }
        float[] texturesArray = new float[verticesCoord.size() * 2];
        float[] normalsArray = new float[verticesCoord.size() * 3];

        for (Face face: faces){
            IndexGroup[] faceVertexIndices = face.getIndexGroups();
            for (IndexGroup indexValue : faceVertexIndices){
                processFaceVertex(indexValue, texturesCoord, normalsCoord, indices, texturesArray, normalsArray);
            }
        }
        int[] indicesArray = indices.stream().mapToInt((Integer v) -> v).toArray();
        return new Mesh(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    /**
     * Groups and order vertices indices, texture position and normal  vector to correctly represent a face
     * @param indices Vertices indices
     * @param texturesCoord Texture position
     * @param normalsCoord Normal Vector
     * @param indicesList Resulting indices list
     * @param texturesArray Resulting texture array
     * @param normalsArray Resulting normal vectors array
     */
    private static void processFaceVertex(IndexGroup indices, List<Vector2f> texturesCoord, List<Vector3f> normalsCoord,
                                          List<Integer> indicesList, float[] texturesArray, float[] normalsArray) {
        int positionIndex = indices.indexPosition;
        indicesList.add(positionIndex);

        if(indices.indexTextureCoordinate >= 0) {
            Vector2f texture = texturesCoord.get(indices.indexTextureCoordinate);
            texturesArray[positionIndex * 2] = texture.x;
            texturesArray[positionIndex * 2 + 1] = 1 - texture.y;
        }

        if(indices.indexVecNormal >= 0) {
            Vector3f normal = normalsCoord.get(indices.indexVecNormal);
            normalsArray[positionIndex * 3] = normal.x;
            normalsArray[positionIndex * 3 + 1] = normal.y;
            normalsArray[positionIndex * 3 + 2] = normal.z;
        }
    }

    /**
     * Stores a face point's vertex, texture and normal vector's indices
     */
    protected static class IndexGroup{
        public static final int NO_VALUE = -1;
        public int indexPosition;
        public int indexTextureCoordinate;
        public int indexVecNormal;

        public IndexGroup(){
            indexPosition = NO_VALUE;
            indexTextureCoordinate = NO_VALUE;
            indexVecNormal = NO_VALUE;
        }
    }

    /**
     * Represents a model's face with three points
     */
    protected static class Face {
        private IndexGroup[] indexGroups = new IndexGroup[3];

        public Face(String v0, String v1, String v2){
            indexGroups[0] = parseLine(v0);
            indexGroups[1] = parseLine(v1);
            indexGroups[2] = parseLine(v2);
        }

        /**
         * parse line from obj file into a polygon's line
         * @param line Extracted text from obj file
         * @return One point data
         */
        public IndexGroup parseLine(String line){
            IndexGroup indexGroup = new IndexGroup();
            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            indexGroup.indexPosition = Integer.parseInt(lineTokens[0]) - 1;
            if(length > 1) {
                String textureCoordinate = lineTokens[1];
                indexGroup.indexTextureCoordinate = textureCoordinate.length() > 0 ?
                        Integer.parseInt(textureCoordinate) - 1 : IndexGroup.NO_VALUE;
                if(length > 2) {
                    indexGroup.indexVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }
            return indexGroup;
        }

        public IndexGroup[] getIndexGroups() {
            return indexGroups;
        }
    }
}

