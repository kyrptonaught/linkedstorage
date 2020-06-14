package net.kyrptonaught.linkedstorage.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.LinkedStorageModClient;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourceNotFoundException;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ZipResourcePack;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Environment(EnvType.CLIENT)
public class InputStreamResourcePack extends ZipResourcePack {

    List<String> files = new ArrayList<>();
private String url;
    public InputStreamResourcePack( String url) {
        super(new File(url));
        this.url = url;
        InputStream stream = LinkedStorageModClient.class.getResourceAsStream(url);
        ZipInputStream reader = new ZipInputStream(stream);
        ZipEntry entry;
        try {
            while ((entry = reader.getNextEntry()) != null)
                files.add(entry.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected InputStream openFile(String name) throws IOException {
        InputStream stream = LinkedStorageModClient.class.getResourceAsStream(url);
        ZipInputStream reader = new ZipInputStream(stream);
        ZipEntry entry;
        try {
            while ((entry = reader.getNextEntry()) != null)
                if (entry.getName().equals(files.get(0) + name))
                    return reader;

        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ResourceNotFoundException(this.base, name);
    }

    @Override
    public boolean containsFile(String name) {
        return files.contains(files.get(0) + name);
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        HashSet<String> set = Sets.newHashSet();
        set.add(LinkedStorageMod.MOD_ID);
        return set;
    }

    @Override
    public Collection<Identifier> findResources(ResourceType type, String namespace, String prefix, int maxDepth, Predicate<String> pathFilter) {
        List<Identifier> list = Lists.newArrayList();
        String string = type.getDirectory() + "/" + namespace + "/";
        String string2 = string + prefix + "/";

        for (String string3 : files) {
            if (!string3.endsWith(".mcmeta") && string3.startsWith(string2)) {
                String string4 = string3.substring(string.length());
                String[] strings = string4.split("/");
                if (strings.length >= maxDepth + 1 && pathFilter.test(strings[strings.length - 1])) {
                    list.add(new Identifier(namespace, string4));
                }
            }
        }
        return list;
    }
}
