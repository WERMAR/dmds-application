<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="ISO-8859-1"/>
  <xsl:template match="poem">
    <html>
      <head>
       <title>
         <xsl:value-of select="/poem/title" />
       </title>
      </head>
      <body>
        <h1>
          <xsl:value-of select="/poem/title" />
        </h1>
        <hr />
        <em>Author:
        <xsl:value-of select="/poem/author" />
        </em>
        <br />
        <xsl:for-each select="/poem/verse">
          <blockquote style="border: 1px solid black; padding 2px; margin-bottom: 10px">
              <ul style="list-style-type: none !important">
                  <xsl:for-each select="./line">
                    <li><xsl:value-of select="." /></li>
                  </xsl:for-each>
              </ul>
          </blockquote>
        </xsl:for-each>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
