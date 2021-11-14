<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="1.0">

  <xsl:output method="html" encoding="iso-8859-1" indent="no"/>

  <xsl:template match="listing">
    <html>
    <head>
      <title>
        Sample Directory Listing For
        <xsl:value-of select="@directory"/>
      </title>
      <style>
        h1{color : white;background-color : #0086b2;}
        h3{color : white;background-color : #0086b2;}
        body{font-family : sans-serif,Arial,Tahoma;
              color : black;background-color : white;}
        b{color : white;background-color : #0086b2;}
        a{color : black;} HR{color : #0086b2;}
      </style>
    </head>
    <body>
      <h1>Directory Listing For
            <xsl:value-of select="@directory"/>
      </h1>
      <hr size="1" />
      <table cellspacing="0"
                  width="100%"
            cellpadding="5"
                  align="center">
        <tr>
          <th align="left">Filename</th>
          <th align="right">Size</th>
          <th align="right">Last Modified</th>
        </tr>
        <xsl:apply-templates select="entries"/>
        </table>
      <xsl:apply-templates select="readme"/>
      <hr size="1" />
      <h3>Apache Tomcat/9.0</h3>
    </body>
    </html>
  </xsl:template>


  <xsl:template match="entries">
    <tr>
      <td align="left">
        <xsl:variable name="urlPath" select="@urlPath"/>
        <a href="..">..</a>
      </td>
    </tr>
    <xsl:apply-templates select="entry"/>
  </xsl:template>

  <xsl:template match="readme">
    <hr size="1" />
    <pre><xsl:apply-templates/></pre>
  </xsl:template>

  <xsl:template match="entry">
    <tr>
      <td align="left">
        <xsl:variable name="urlPath" select="@urlPath"/>
        <a href="{$urlPath}">
          <tt><xsl:apply-templates/></tt>
        </a>
      </td>
      <td align="right">
        <tt><xsl:value-of select="@size"/></tt>
      </td>
      <td align="right">
        <tt><xsl:value-of select="@date"/></tt>
      </td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
