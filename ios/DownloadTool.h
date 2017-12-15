//
//  DownloadTool.h
//  HotUpdateTest
//
//  Created by 王泽 on 2017/12/13.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface DownLoadTool : NSObject

@property (nonatomic, strong) NSString *zipPath;

@property (nonatomic, strong) UIView*  view;

+ (DownLoadTool *) defaultDownLoadTool;

//根据url下载相关文件
-(void)downLoadWithUrl:(NSString*)url;
//解压压缩包
-(BOOL)unZip;
//删除压缩包
-(void)deleteZip;

@end
