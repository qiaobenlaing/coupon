//
//  RefundBackCardViewController.m
//  BMSQS
//
//  Created by guohong on 16/3/30.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "RefundBackCardViewController.h"
#import "MyBackCardCell.h"

@interface RefundBackCardViewController () <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong)UITableView* tableView;

@end

@implementation RefundBackCardViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViweUp];
    
    
}

- (void)setViweUp {
    
    [self setNavTitle:@"选择银行卡"];
    [self setNavBackItem];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    [self.view addSubview:self.tableView];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    
    [self.view addSubview:self.tableView];
    
}


#pragma mark -  UITableView delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataAry.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *identifier = @"MyBackCardCell";
    MyBackCardCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[MyBackCardCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        
    }
    cell.backgroundColor = [UIColor whiteColor];
    cell.selectionStyle=UITableViewCellSelectionStyleNone ;
    [cell setCellValue:[self.dataAry objectAtIndex:indexPath.row]];
    
    return cell;

}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [[NSNotificationCenter defaultCenter]postNotificationName:@"selectBackCardFinsh" object:[self.dataAry objectAtIndex:indexPath.row]];
    [self.navigationController popViewControllerAnimated:YES];

}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 62;
}


@end
