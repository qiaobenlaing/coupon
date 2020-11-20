//
//  plistFileOperation.h
//  IcardEnglish
//
//  Created by djx on 12-7-2.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface cacheFileOperation : NSObject {

}

+ (id)readFromPlistFile:(NSString*)plistName;
+ (BOOL)createPlistFile:(NSString*)plistFileName;
+ (BOOL)writeToPlistFile:(NSString*)plistName content:(id)plistContent key:(NSString*)nKey;
+ (NSString*)readFromPlistFile:(NSString*)plistName key:(NSString*)nkey;
+ (BOOL)writeDataToFile:(NSData*)data fileName:(NSString*)name;
+ (NSData*)readDataByName:(NSString*)name;
+ (BOOL)removeFileByName:(NSString*)name;
+ (BOOL)fileIsExistsByName:(NSString*)name;
+ (NSString*)getDocumentPathByFileName:(NSString*)fileName ofType:(NSString*)type;
+ (BOOL)fileTmpIsExistsByName:(NSString*)name;
+ (BOOL)removeTmpFileByName:(NSString *)name ofType:(NSString *)_type;
/**
 生成放在缓存里面文件路径
 @param _fileName 文件名
 @param _type 文件类型
 @returns 文件路径
 */
+ (NSString*)getPathByFileName:(NSString *)_fileName ofType:(NSString *)_type;
@end
