<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output indent="yes"/>
    <xsl:template match="*[local-name() = 'Result']">
	    <map url="{./text()}"/>
    </xsl:template>
</xsl:stylesheet>
