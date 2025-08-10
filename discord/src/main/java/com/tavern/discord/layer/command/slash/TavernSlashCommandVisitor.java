package com.tavern.discord.layer.command.slash;

public interface TavernSlashCommandVisitor {
    void visit(SimpleTavernSlashCommand command);
    void visit(StructuredTavernSlashCommand command);
}
