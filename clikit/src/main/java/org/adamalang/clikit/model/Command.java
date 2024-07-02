/*
* Adama Platform and Language
* Copyright (C) 2021 - 2024 by Adama Platform Engineering, LLC
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published
* by the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Affero General Public License for more details.
* 
* You should have received a copy of the GNU Affero General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.adamalang.clikit.model;

import org.adamalang.clikit.exceptions.XMLFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

/** A command is a representation of an instruction for communctation with Adama's backend **/
public class Command {
    public final Argument[] argList;
    public final String output;
    public final String capName;
    public final String name;
    public final String documentation;
    public final boolean danger;
    public final String camel;

    public Command(String name, String documentation, String output, boolean danger, Argument[] argList) {
        name = name.toLowerCase(Locale.ROOT);
        output = (output == null) ? null : output.toLowerCase(Locale.ROOT);
        this.name = name;
        this.camel = Common.camelize(name, true);
        this.capName = Common.camelize(name);
        this.documentation = documentation;
        this.argList = argList;
        this.output = output;
        this.danger = danger;
    }

    public static Command[] createCommandList(NodeList nodeList, XMLFormatException givenException, Map<String, ArgumentDefinition> arguments) throws Exception{
        ArrayList<Command> commandArray = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node commandNode = nodeList.item(i);
            String filePos = "line " + commandNode.getUserData("lineNumber") + " column " + commandNode.getUserData("colNumber");
            Element commandElem = (Element) commandNode;
            String commandName = commandElem.getAttribute("name");
            if (commandName == null || commandName.trim().isEmpty())
                givenException.addToExceptionStack("The 'command' node at " + filePos + " is missing name attribute");
            Argument[] argumentList = Argument.createArgumentList(commandElem.getElementsByTagName("arg"), givenException, arguments);
            boolean danger = commandElem.getAttribute("warn").equals("") ? false : true;
            String groupDocumentation = Common.getDocumentation(commandElem, givenException);
            String methodType = commandElem.getAttribute("method");
            if ( methodType == null || "".equals(methodType.trim()))
                methodType = "self";
            String outputArg = "";
            if (commandElem.hasAttribute("output")) {
                outputArg = commandElem.getAttribute("output").toLowerCase();
            } else {
                outputArg = "yes";
            }

            Command command = new Command(commandName, groupDocumentation, outputArg, danger, argumentList);
            commandArray.add(command);
        }
        commandArray.sort(Comparator.comparing(a -> a.name));
        return commandArray.toArray(new Command[commandArray.size()]);
    }

    public static Command[] createCommandList(NodeList nodeList, XMLFormatException givenException, Map<String, ArgumentDefinition> arguments, String parent) throws Exception{
        ArrayList<Command> commandArray = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node commandNode = nodeList.item(i);
            String filePos = "line " + commandNode.getUserData("lineNumber") + " column " + commandNode.getUserData("colNumber");
            Element commandElem = (Element) commandNode;
            // Will always have parent
            Node parentNode = commandElem.getParentNode();
            if (!parentNode.getNodeName().equals(parent)) {
                continue;
            }

            String commandName = commandElem.getAttribute("name");
            if (commandName == null || commandName.trim().isEmpty())
                givenException.addToExceptionStack("The 'command' node at " + filePos + " is missing name attribute");
            Argument[] argumentList = Argument.createArgumentList(commandElem.getElementsByTagName("arg"), givenException, arguments);
            boolean danger = commandElem.getAttribute("warn").equals("") ? false : true;
            String groupDocumentation = Common.getDocumentation(commandElem, givenException);
            String methodType = commandElem.getAttribute("method");
            if ( methodType == null || "".equals(methodType.trim()))
                methodType = "self";
            String outputArg = "";
            if (commandElem.hasAttribute("output")) {
                outputArg = commandElem.getAttribute("output").toLowerCase();
            } else {
                outputArg = "yes";
            }

            Command command = new Command(commandName, groupDocumentation, outputArg, danger, argumentList);
            commandArray.add(command);
        }
        commandArray.sort(Comparator.comparing(a -> a.name));
        return commandArray.toArray(new Command[commandArray.size()]);
    }
}
