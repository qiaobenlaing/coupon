//
//  CustomNotificationView.m
//  BMSQC
//
//  Created by liuqin on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "CustomNotificationView.h"

@interface CustomNotificationView ()<UITableViewDataSource,UITableViewDelegate>


@end



@implementation CustomNotificationView


-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backgroundColor = APP_NAVCOLOR;
        self.notiArray = [[NSMutableArray alloc]init];
        self.userInteractionEnabled = YES;
        
        [self.notiArray addObject:@{@"webUrl":@"http://www.baidu.com",@"content":@"aaaaaaa"}];
        [self.notiArray addObject:@{@"webUrl":@"http://www.biying.com",@"content":@"bbbbbbb"}];
        
        self.notiTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, 20)];
        self.notiTableView.delegate = self;
        self.notiTableView.dataSource = self;
        self.notiTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        
        [self addSubview:self.notiTableView];
        
    }
    return self;
}


-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *idenfier = @"notiCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:idenfier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenfier];
        cell.backgroundColor = [UIColor clearColor];
        cell.textLabel.backgroundColor = [UIColor clearColor];
        
    }
    
    NSDictionary *dic = [self.notiArray objectAtIndex:indexPath.row];
    cell.textLabel.text =  [dic objectForKey:@"content"];
    cell.textLabel.font = [UIFont systemFontOfSize:12];
    cell.textLabel.textColor = APP_TEXTCOLOR;
    return cell;
    
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return self.notiArray.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 20;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSDictionary *dic = [self.notiArray objectAtIndex:indexPath.row];
    
    [self.notiDelegate presentViewC:dic ];
    
    [self.notiArray removeObjectAtIndex:indexPath.row];
    [self.notiTableView reloadData];
    
    if (self.notiArray.count == 0) {
        [[NSNotificationCenter defaultCenter]postNotificationName:@"deleNotiView" object:nil];
    }
}

-(void)addNoti:(NSDictionary *)dic{
    [self.notiArray addObject:dic];
    [self.notiTableView reloadData];
}

@end
