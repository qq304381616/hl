<#--

Copyright (C) 2011-2016 Markus Junginger, greenrobot (http://greenrobot.org)
                                                                           
This file is part of greenDAO Generator.                                   
                                                                           
greenDAO Generator is free software: you can redistribute it and/or modify 
it under the terms of the GNU General Public License as published by       
the Free Software Foundation, either version 3 of the License, or          
(at your option) any later version.                                        
greenDAO Generator is distributed in the hope that it will be useful,      
but WITHOUT ANY WARRANTY; without even the implied warranty of             
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              
GNU General Public License for more details.                               
                                                                           
You should have received a copy of the GNU General Public License          
along with greenDAO Generator.  If not, see <http://www.gnu.org/licenses/>.

-->
<#-- @ftlvariable name="entity" type="org.greenrobot.greendao.generator.Entity" -->
<#-- @ftlvariable name="schema" type="org.greenrobot.greendao.generator.Schema" -->

<#assign toBindType = {"Boolean":"Long", "Byte":"Long", "Short":"Long", "Int":"Long", "Long":"Long", "Float":"Double", "Double":"Double", "String":"String", "ByteArray":"Blob", "Date": "Long" } />
<#assign toCursorType = {"Boolean":"Short", "Byte":"Short", "Short":"Short", "Int":"Int", "Long":"Long", "Float":"Float", "Double":"Double", "String":"String", "ByteArray":"Blob", "Date": "Long"  } />
package ${entity.javaPackageDao};

<#if entity.toOneRelations?has_content || entity.incomingToManyRelations?has_content>
import java.util.List;
</#if>
<#if entity.toOneRelations?has_content>
import java.util.ArrayList;
</#if>
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.hl.greendao.core.AbstractDao;
import com.hl.greendao.core.Property;
<#if entity.toOneRelations?has_content>
import com.hl.greendao.core.internal.SqlUtils;
</#if>
import com.hl.greendao.core.internal.DaoConfig;
import com.hl.greendao.core.database.Database;
import com.hl.greendao.core.database.DatabaseStatement;
<#if entity.incomingToManyRelations?has_content>
import com.hl.greendao.core.query.Query;
import com.hl.greendao.core.query.QueryBuilder;
</#if>

<#if entity.javaPackageDao != schema.defaultJavaPackageDao>
import ${schema.defaultJavaPackageDao}.${schema.prefix}DaoSession;

</#if>
<#if entity.additionalImportsDao?has_content>
<#list entity.additionalImportsDao as additionalImport>
import ${additionalImport};
</#list>

</#if>
<#if entity.javaPackageDao != entity.javaPackage>
import ${entity.javaPackage}.${entity.className};

</#if>
<#if entity.protobuf>
import ${entity.javaPackage}.${entity.className}.Builder;

</#if>
// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "${entity.dbName}".
*/
public class ${entity.classNameDao} extends AbstractDao<${entity.className}, ${entity.pkType}> {

    public static final String TABLENAME = "${entity.dbName}";

    /**
     * Properties of entity ${entity.className}.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
<#list entity.propertiesColumns as property>
        public final static Property ${property.propertyName?cap_first} = new Property(${property_index}, ${property.javaType}.class, "${property.propertyName}", ${property.primaryKey?string}, "${property.dbName}");
</#list>
    }

<#if entity.active>
    private ${schema.prefix}DaoSession daoSession;

</#if>
<#list entity.properties as property><#if property.customType?has_content><#--
-->    private final ${property.converterClassName} ${property.propertyName}Converter = new ${property.converterClassName}();
</#if></#list>
<#list entity.incomingToManyRelations as toMany>
    private Query<${toMany.targetEntity.className}> ${toMany.sourceEntity.className?uncap_first}_${toMany.name?cap_first}Query;
</#list>

    public ${entity.classNameDao}(DaoConfig config) {
        super(config);
    }
    
    public ${entity.classNameDao}(DaoConfig config, ${schema.prefix}DaoSession daoSession) {
        super(config, daoSession);
<#if entity.active>        
        this.daoSession = daoSession;
</#if>
    }

<#if !entity.skipCreationInDb>
    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"${entity.dbName}\" (" + //
<#list entity.propertiesColumns as property>
                "\"${property.dbName}\" ${property.dbType}<#if property.constraints??> ${property.constraints} </#if><#if property_has_next>," +<#else>);");</#if> // ${property_index}: ${property.propertyName}
</#list>
<#if entity.indexes?has_content >
        // Add Indexes
<#list entity.indexes as index>
        db.execSQL("CREATE <#if index.unique>UNIQUE </#if>INDEX " + constraint + "${index.name} ON \"${entity.dbName}\"" +
                " (<#list index.properties 
as property>\"${property.dbName}\"<#if (index.propertiesOrder[property_index])??> ${index.propertiesOrder[property_index]}</#if><#sep>,</#list>);");
</#list>
</#if>         
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"${entity.dbName}\"";
        db.execSQL(sql);
    }

</#if>
<#assign stmtTypes = ["DatabaseStatement", "SQLiteStatement"] />
<#list stmtTypes as stmtType>
    @Override
    protected final void bindValues(${stmtType} stmt, ${entity.className} entity) {
        stmt.clearBindings();
<#list entity.properties as property>
<#if property.notNull || entity.protobuf>
<#if entity.protobuf>
        if(entity.has${property.propertyName?cap_first}()) {
    </#if>        stmt.bind${toBindType[property.propertyType]}(${property_index + 1}, ${property.databaseValueExpressionNotNull});
<#if entity.protobuf>
        }
</#if>
<#else> <#-- nullable, non-protobuff -->
        ${property.javaTypeInEntity} ${property.propertyName} = entity.get${property.propertyName?cap_first}();
        if (${property.propertyName} != null) {
            stmt.bind${toBindType[property.propertyType]}(${property_index + 1}, ${property.databaseValueExpression});
        }
</#if>
</#list>
<#list entity.toOneRelations as toOne>
<#if !toOne.fkProperties?has_content>

        ${toOne.targetEntity.className} ${toOne.name} = entity.peak${toOne.name?cap_first}();
        if(${toOne.name} != null) {
            ${toOne.targetEntity.pkProperty.javaType} ${toOne.name}__targetKey = ${toOne.name}.get${toOne.targetEntity.pkProperty.propertyName?cap_first}();
<#if !toOne.targetEntity.pkProperty.notNull>
            if(${toOne.name}__targetKey != null) {
                // TODO bind ${toOne.name}__targetKey
            }
<#else>
            // TODO bind ${toOne.name}__targetKey
</#if>
        }
</#if>
</#list>
    }

</#list>
<#if entity.active && !entity.protobuf>
    @Override
    protected final void attachEntity(${entity.className} entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

</#if>
    @Override
    public ${entity.pkType} readKey(Cursor cursor, int offset) {
<#if entity.pkProperty??>
        return <#if !entity.pkProperty.notNull>cursor.isNull(offset + ${entity.pkProperty.ordinal}) ? null : </#if><#if
            entity.pkProperty.propertyType == "Byte">(byte) </#if><#if
            entity.pkProperty.propertyType == "Date">new java.util.Date(</#if>cursor.get${toCursorType[entity.pkProperty.propertyType]}(offset + ${entity.pkProperty.ordinal})<#if
            entity.pkProperty.propertyType == "Boolean"> != 0</#if><#if
            entity.pkProperty.propertyType == "Date">)</#if>;
<#else>
        return null;
</#if>  
    }    

    @Override
    public ${entity.className} readEntity(Cursor cursor, int offset) {
<#if entity.protobuf>
        Builder builder = ${entity.className}.newBuilder();
<#list entity.properties as property>
<#if !property.notNull>
        if (!cursor.isNull(offset + ${property_index})) {
    </#if>        builder.set${property.propertyName?cap_first}(cursor.get${toCursorType[property.propertyType]}(offset + ${property_index}));
<#if !property.notNull>
        }
</#if>        
</#list>        
        return builder.build();
<#elseif entity.constructors>
<#--
############################## readEntity non-protobuff, constructor ############################## 
-->
        ${entity.className} entity = new ${entity.className}( //
<#list entity.properties as property>
            <#if !property.notNull>cursor.isNull(offset + ${property_index}) ? null : </#if><#--
            -->${property.getEntityValueExpression("cursor.get${toCursorType[property.propertyType]}(offset + ${property_index})")}<#--
            --><#if property_has_next>,</#if> // ${property.propertyName}
</#list>        
        );
        return entity;
<#else>
<#--
############################## readEntity non-protobuff, setters ############################## 
-->
        ${entity.className} entity = new ${entity.className}();
        readEntity(cursor, entity, offset);
        return entity;
</#if>
    }
     
    @Override
    public void readEntity(Cursor cursor, ${entity.className} entity, int offset) {
<#if entity.protobuf>
        throw new UnsupportedOperationException("Protobuf objects cannot be modified");
<#else> 
<#list entity.properties as property>
        entity.set${property.propertyName?cap_first}(<#if !property.notNull>cursor.isNull(offset + ${property_index}) ? null : </#if><#--
            -->${property.getEntityValueExpression("cursor.get${toCursorType[property.propertyType]}(offset + ${property_index})")});
</#list>
</#if>
     }
    
    @Override
    protected final ${entity.pkType} updateKeyAfterInsert(${entity.className} entity, long rowId) {
<#if entity.pkProperty??>
<#if entity.pkProperty.propertyType == "Long">
<#if !entity.protobuf>
        entity.set${entity.pkProperty.propertyName?cap_first}(rowId);
</#if>
        return rowId;
<#else>
        return entity.get${entity.pkProperty.propertyName?cap_first}();
</#if>
<#else>
        // Unsupported or missing PK type
        return null;
</#if>
    }
    
    @Override
    public ${entity.pkType} getKey(${entity.className} entity) {
<#if entity.pkProperty??>
        if(entity != null) {
            return entity.get${entity.pkProperty.propertyName?cap_first}();
        } else {
            return null;
        }
<#else>
        return null;
</#if>    
    }

    @Override
    public boolean hasKey(${entity.className} entity) {
<#if entity.pkProperty??>
<#if entity.pkProperty.notNull>
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
<#else>
<#if entity.protobuf>
        return entity.has${entity.pkProperty.propertyName?cap_first}();
<#else>
        return entity.get${entity.pkProperty.propertyName?cap_first}() != null;
</#if>
</#if>
<#else>
        // TODO
        return false;
</#if>
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return ${(!entity.protobuf)?string};
    }
    
<#list entity.incomingToManyRelations as toMany>
    /** Internal query to resolve the "${toMany.name}" to-many relationship of ${toMany.sourceEntity.className}. */
    public List<${toMany.targetEntity.className}> _query${toMany.sourceEntity.className?cap_first}_${toMany.name?cap_first}(<#--
    --><#if toMany.targetProperties??><#list toMany.targetProperties as property><#--
    -->${property.javaType} ${property.propertyName}<#if property_has_next>, </#if></#list><#else><#--
    -->${toMany.sourceProperty.javaType} ${toMany.sourceProperty.propertyName}</#if>) {
        synchronized (this) {
            if (${toMany.sourceEntity.className?uncap_first}_${toMany.name?cap_first}Query == null) {
                QueryBuilder<${toMany.targetEntity.className}> queryBuilder = queryBuilder();
<#if toMany.targetProperties??>
    <#list toMany.targetProperties as property>
                queryBuilder.where(Properties.${property.propertyName?cap_first}.eq(null));
    </#list>
<#else>
                queryBuilder.join(${toMany.joinEntity.className}.class, ${toMany.joinEntity.classNameDao}.Properties.${toMany.targetProperty.propertyName?cap_first})
                    .where(${toMany.joinEntity.classNameDao}.Properties.${toMany.sourceProperty.propertyName?cap_first}.eq(${toMany.sourceProperty.propertyName}));
</#if>
<#if toMany.order?has_content>
                queryBuilder.orderRaw("${toMany.order}");
</#if>
                ${toMany.sourceEntity.className?uncap_first}_${toMany.name?cap_first}Query = queryBuilder.build();
            }
        }
        Query<${toMany.targetEntity.className}> query = ${toMany.sourceEntity.className?uncap_first}_${toMany.name?cap_first}Query.forCurrentThread();
<#if toMany.targetProperties??>
    <#list toMany.targetProperties as property>
        query.setParameter(${property_index}, ${property.propertyName});
    </#list>
<#else>
        query.setParameter(0, ${toMany.sourceProperty.propertyName});
</#if>
        return query.list();
    }

</#list>   
<#if entity.toOneRelations?has_content>
    <#include "dao-deep.ftl">
</#if>
}
