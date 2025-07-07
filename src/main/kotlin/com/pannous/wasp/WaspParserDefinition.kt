package com.pannous.wasp

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class WaspParserDefinition : ParserDefinition {
    
    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create(Token.COMMENT)
        val STRINGS = TokenSet.create(Token.STRING)
        val FILE = IFileElementType("WASP_FILE", WaspLanguage.INSTANCE)
    }
    
    override fun createLexer(project: Project?): Lexer = WaspLexer()
    
    override fun createParser(project: Project?): PsiParser = WaspParser()
    
    override fun getFileNodeType(): IFileElementType = FILE
    
    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES
    
    override fun getCommentTokens(): TokenSet = COMMENTS
    
    override fun getStringLiteralElements(): TokenSet = STRINGS
    
    override fun createElement(node: ASTNode): PsiElement = WaspPsiElement(node)
    
    override fun createFile(viewProvider: FileViewProvider): PsiFile = WaspFile(viewProvider)
}
