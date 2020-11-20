//
//  plistFileOperation.m
//  IcardEnglish
//
//  Created by djx on 12-7-2.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import "cacheFileOperation.h"

@implementation cacheFileOperation

+ (BOOL)createPlistFile:(NSString*)plistFileName
{
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
    NSString *path = [paths objectAtIndex:0];  
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
    NSString *filename = [pathDocument stringByAppendingPathComponent:plistFileName];
	
	if (![[NSFileManager defaultManager]fileExistsAtPath:filename]) 
	{
		NSFileManager* fm = [NSFileManager defaultManager];
        return [fm createFileAtPath:filename contents:nil attributes:nil];
	}
	else
	{
		return YES;
	}

	
}

+ (BOOL)writeToPlistFile:(NSString*)plistName content:(id)plistContent key:(NSString*)nKey
{
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
    NSString *path = [paths  objectAtIndex:0];  
	
	//路径＋文件名　作为文件的存放路径
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
	NSFileManager* nsfm = [NSFileManager defaultManager];
	if (![nsfm fileExistsAtPath:pathDocument])
	{
		[nsfm createDirectoryAtPath:pathDocument withIntermediateDirectories:YES attributes:nil error:nil];
	}
	
    NSString *filename = [pathDocument stringByAppendingPathComponent:plistName];   //获取路径
    
	NSMutableDictionary *dic = [ NSMutableDictionary dictionaryWithContentsOfFile:filename];
	
	if (dic == nil)
	{
		dic = [[NSMutableDictionary alloc]init];
	}
	
	[dic setObject:plistContent forKey:nKey];
    //创建一个dic，写到plist文件里
   // dic = [NSMutableDictionary dictionaryWithObjectsAndKeys:plistContent,nKey,nil]; //写入数据
    return [dic writeToFile:filename atomically:YES]; 
}

+ (NSString*)readFromPlistFile:(NSString*)plistName key:(NSString*)nkey
{
	NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	NSString* path = [paths objectAtIndex:0];
	
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
	
	NSString* fileName = [pathDocument stringByAppendingPathComponent:plistName];
	
	//NSString* fileName = [path stringByAppendingPathComponent:plistName];
	
	NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithContentsOfFile:fileName];
	if ([dic count] > 0)
	{
		return [dic objectForKey:nkey];
	}
	
	return nil;
}

+ (id)readFromPlistFile:(NSString*)plistName
{
	NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	NSString* path = [paths objectAtIndex:0];
	
	//路径＋文件名　作为文件的存放路径
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
	
	NSString* fileName = [pathDocument stringByAppendingPathComponent:plistName];
	
	 id dic = [NSMutableDictionary dictionaryWithContentsOfFile:fileName];
	
	return dic;
	
}

+ (BOOL)writeDataToFile:(NSData*)data fileName:(NSString*)name
{
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
	NSString *path = [paths objectAtIndex:0];  
	
	//路径＋文件名　作为文件的存放路径
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
	NSFileManager* nsfm = [NSFileManager defaultManager];
	if (![nsfm fileExistsAtPath:pathDocument])
	{
		[nsfm createDirectoryAtPath:pathDocument withIntermediateDirectories:YES attributes:nil error:nil];
	}
	
	NSString *filename = [pathDocument stringByAppendingPathComponent:name];   //获取路径
	
	BOOL isWrite = [data writeToFile:filename atomically:YES];
	data = nil;
	
	return isWrite;
}


+ (NSData*)readDataByName:(NSString*)name
{
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
	NSString *path = [paths objectAtIndex:0];  
	//路径＋文件名　作为文件的存放路径
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
	
	NSString* fileName = [pathDocument stringByAppendingPathComponent:name];
	
	//NSString *filename = [path stringByAppendingPathComponent:name];   //获取路径
	//[[NSData alloc]initWithContentsOfFile:fileName];
	return [NSData dataWithContentsOfFile:fileName];
}

+ (BOOL)removeFileByName:(NSString*)name
{
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
	NSString *path = [paths objectAtIndex:0];  
	//路径＋文件名　作为文件的存放路径
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
	
	NSString* fileName = [pathDocument stringByAppendingPathComponent:name];
	return [[NSFileManager defaultManager]removeItemAtPath:fileName error:nil];
}

+ (BOOL)fileIsExistsByName:(NSString*)name
{
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
	NSString *path = [paths objectAtIndex:0];  
	//路径＋文件名　作为文件的存放路径
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
	
	NSString* fileName = [pathDocument stringByAppendingPathComponent:name];
	NSFileManager* nsfm = [NSFileManager defaultManager];
	return [nsfm fileExistsAtPath:fileName];
}

+ (NSString*)getDocumentPathByFileName:(NSString*)fileName ofType:(NSString*)type
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
	NSString *path = [paths objectAtIndex:0];
	//路径＋文件名　作为文件的存放路径
	NSString* pathDocument = [NSString stringWithFormat:@"%@/%@",path,APP_SAVEFILE_PATH];
	
	NSString* fileDirectory = [[pathDocument stringByAppendingPathComponent:fileName] stringByAppendingPathExtension:type];
    return fileDirectory;
}

/**
 生成放在缓存里面文件路径
 @param _fileName 文件名
 @param _type 文件类型
 @returns 文件路径
 */
+ (NSString*)getPathByFileName:(NSString *)_fileName ofType:(NSString *)_type
{
    NSString *home = NSHomeDirectory();
    NSString *tempPath = [home stringByAppendingPathComponent:@"tmp"];
    
    //NSString* cachePath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)objectAtIndex:0];
    NSString* fileDirectory = [[tempPath stringByAppendingPathComponent:_fileName]stringByAppendingPathExtension:_type];
    return fileDirectory;
}

+ (BOOL)removeTmpFileByName:(NSString *)name ofType:(NSString *)_type
{
    NSString *home = NSHomeDirectory();
    NSString *tempPath = [home stringByAppendingPathComponent:@"tmp"];
    
    //NSString* cachePath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES)objectAtIndex:0];
    NSString* fileDirectory = [[tempPath stringByAppendingPathComponent:name ]stringByAppendingPathExtension:_type];
    
	return [[NSFileManager defaultManager]removeItemAtPath:fileDirectory error:nil];
}

+ (BOOL)fileTmpIsExistsByName:(NSString*)name
{
	NSString *home = NSHomeDirectory();
    NSString *tempPath = [home stringByAppendingPathComponent:@"tmp"];
    NSString* fileName = [tempPath stringByAppendingPathComponent:name];
	NSFileManager* nsfm = [NSFileManager defaultManager];
    
	return [nsfm fileExistsAtPath:fileName];
}
@end
