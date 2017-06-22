package com.palmap.library.command;

import java.util.ArrayDeque;

/**
 * Created by 王天明 on 2016/6/13.
 */
public class CommandManager {

    /**
     * 最大存储命令数量
     * 使用堆栈不能无限容量
     */
    private int maxCommand;

    private static final int DEFAULTMAX = 10;

    /**
     * 执行过的命令
     */
    private ArrayDeque<Command> doCommand;

    /**
     * 取消过的命令
     */
    private ArrayDeque<Command> unDoCommand;

    public CommandManager() {
        this(DEFAULTMAX);
    }

    public CommandManager(int maxCommand) {
        doCommand = new ArrayDeque<>();
        unDoCommand = new ArrayDeque<>();
        this.maxCommand = maxCommand;
    }

    public void doCommand(Command command) {
        command.toDo();
        pushDoCommand(command);
    }

    /**
     * 恢复上一条命令
     */
    public void unDoCommand() {
        if (!doCommand.isEmpty()) {
            Command command = doCommand.pop();
            command.unDo();
            pushUnDoCommand(command);
        }
    }

    /**
     * 重新执行上一条命令
     */
    public void reDoCommand(){
        if (!doCommand.isEmpty()) {
            Command command = doCommand.peek();
            command.reDo();
        }
    }

    private void pushUnDoCommand(Command command) {
        if (unDoCommand.size() >= maxCommand) {
            unDoCommand.removeLast();
        }
        unDoCommand.addFirst(command);
    }

    private void pushDoCommand(Command command) {
        if (doCommand.size() >= maxCommand) {
            doCommand.removeLast();
        }
        doCommand.addFirst(command);
    }

}