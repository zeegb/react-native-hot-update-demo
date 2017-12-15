//
//  UpdateDataLoader.m
//  HotUpdateTest
//
//  Created by 王泽 on 2017/12/13.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "UpdateDataLoader.h"
#import "DownloadTool.h"
#import "AFHTTPSessionManager.h"

@implementation UpdateDataLoader
+ (UpdateDataLoader *) sharedInstance
{
  static UpdateDataLoader *sharedInstance = nil;
  static dispatch_once_t onceToken;
  
  dispatch_once(&onceToken, ^{
    sharedInstance = [[UpdateDataLoader alloc] init];
  });
  
  return sharedInstance;
}
//创建bundle路径
-(void)createPath{
  NSLog(@"创建bundle路径");
  NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
  NSString *path = [paths lastObject];
  
  NSFileManager *fileManager = [NSFileManager defaultManager];
  NSString *directryPath = [path stringByAppendingPathComponent:@"IOSBundle"];
  [fileManager createDirectoryAtPath:directryPath withIntermediateDirectories:YES attributes:nil error:nil];
  NSString *filePath = [directryPath stringByAppendingPathComponent:@"Version.plist"];
  [fileManager createFileAtPath:filePath contents:nil attributes:nil];
}
//获取版本信息
-(void)getAppVersion{
  
  //从服务器上获取版本信息,与本地plist存储的版本进行比较
  //假定返回的结果集
  /*{
   bundleVersion = 2;
   downloadUrl = "www.baidu.com";
   }*/
  
  //1.获取本地plist文件的版本号 假定为2
  NSString* plistPath=[self getVersionPlistPath];
  NSMutableDictionary *data = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
  
  
  NSInteger localV=[data[@"bundleVersion"]integerValue];
  AFHTTPSessionManager *mgr = [AFHTTPSessionManager manager];
  
  [mgr GET:@"http://11.11.11.11:9999/vest/getCurrentVersion/ios" parameters:nil success:^(NSURLSessionDataTask *task, id responseObject) {
    NSLog(@"请求成功---%@", responseObject);
    NSInteger serviceV=3;
    if(serviceV>localV){
      //下载bundle文件 存储在 Doucuments/IOSBundle/下
      NSString*url=@"http://119.29.244.84:9091/bundle.zip";
      [[DownLoadTool defaultDownLoadTool] downLoadWithUrl:url];
    }
  } failure:^(NSURLSessionDataTask *task, NSError *error) {
    NSLog(@"请求失败---%@", error);
  }];
}

//获取Bundle 路径
-(NSString*)iOSFileBundlePath{
  //获取沙盒路径
  NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
  NSString* path = [paths objectAtIndex:0];
  NSLog(@"the save version file's path is :%@",path);
  //填写文件名
  NSString* filePath = [path stringByAppendingPathComponent:@"/IOSBundle/bundle"];
  return  filePath;
}

//获取版本信息储存的文件路径
-(NSString*)getVersionPlistPath{
  //获取沙盒路径
  NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
  NSString* path = [paths objectAtIndex:0];
  NSLog(@"the save version file's path is :%@",path);
  //填写文件名
  NSString* filePath = [path stringByAppendingPathComponent:@"/IOSBundle/Version.plist"];
  NSLog(@"文件路径为：%@",filePath);
  return filePath;
}

//创建或修改版本信息
-(void)writeAppVersionInfoWithDictiony:(NSDictionary*)dictionary{
  
  NSString* filePath  = [self getVersionPlistPath];
  [dictionary writeToFile:filePath atomically:YES];
}
@end
