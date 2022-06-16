<?xml version="1.0"?>
<xsl:stylesheet xmlns:ili="http://www.interlis.ch/INTERLIS2.3" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output encoding="UTF-8" indent="yes" method="html"/>
    <xsl:template match="/XXXXXXXXXXXXXXXXXX">
        <html>
        <head>
        <title>foo • bar</title>
        <meta name="description" content="INTERLIS Repository Mirror"/>
        <meta name="author" content="Stefan Ziegler" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <meta http-equiv="cache-control" content="no-cache"/>

        </head>
        <body>

            <xsl:apply-templates select="ili:TRANSFER/ili:DATASECTION/ili:IliRepository20.RepositoryIndex" />

        </body>
        </html>
    </xsl:template>

    <xsl:template match="/">
        <xsl:call-template name="loop">
            <xsl:with-param name="count" select="10000"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="loop">
        <xsl:param name="count" select="1"/>
        <xsl:if test="$count > 0">
            <xsl:text> </xsl:text>
            <xsl:value-of select="$count"/>  
            <xsl:call-template name="loop">
                <xsl:with-param name="count" select="$count - 1"/>
            </xsl:call-template>
        </xsl:if>    
    </xsl:template>

<!-- BILLIG: ein paar for-each-group (max circa 5-10). substring-before gruppieren oder so ähnlich.-->

    <xsl:template match="ili:IliRepository20.RepositoryIndex">  
        <!-- <xsl:for-each-group select="ili:IliRepository20.RepositoryIndex.ModelMetadata" group-by="string-join(remove(tokenize(ili:File7,'/'),last()),_)"> -->
        <!-- <xsl:for-each select="ili:IliRepository20.RepositoryIndex.ModelMetadata"> -->
            <div>
            <!--
                <xsl:value-of select="tokenize(., '/')"/>
                <xsl:variable name="articleArr" select="tokenize(.,'/')" />
                <br/>
                <xsl:for-each select="$articleArr">
                    <xsl:value-of select="." />
                    <br/>
                </xsl:for-each>
                                    <br/><br/>
-->

            <!-- <xsl:value-of select="current-group()" />

            <br/><br/>

            <xsl:message>foo<xsl:value-of select="." /></xsl:message> -->

            <!-- <xsl:variable name="filenameElements" select="tokenize(ili:File,'/')" />
            <xsl:message><xsl:value-of select="count($filenameElements)" /></xsl:message>
            <xsl:message><xsl:value-of select="$filenameElements[last()-1]" /></xsl:message>

            <xsl:variable name="pathElements" select="remove($filenameElements,last())" />
            <xsl:message>foo<xsl:value-of select="$pathElements[3]" /></xsl:message>

            <xsl:message>bar<xsl:value-of select="remove(tokenize(ili:File,'/'),last())[2]" /></xsl:message>
 -->


            <!-- <xsl:message><xsl:value-of select="count(tokenize(ili:File,'/'))" /></xsl:message>

            <xsl:message><xsl:value-of select="remove(tokenize(ili:File,'/'), count(tokenize(ili:File,'/')))" /></xsl:message>
            <xsl:message><xsl:value-of select="remove(tokenize(ili:File,'/'), last())" /></xsl:message>
            <xsl:message><xsl:value-of select="tokenize(ili:File,'/')" /></xsl:message>
            <xsl:message><xsl:value-of select="ili:File" /></xsl:message> -->
            <!-- <xsl:message><xsl:value-of select="." /></xsl:message> -->

            </div>

            <xsl:message>Hallo Welt.</xsl:message>
        <!-- </xsl:for-each> -->
        <!-- </xsl:for-each-group> -->
    </xsl:template>


</xsl:stylesheet>