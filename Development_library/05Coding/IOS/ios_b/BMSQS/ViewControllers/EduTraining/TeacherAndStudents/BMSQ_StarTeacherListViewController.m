//
//  BMSQ_StarTeacherListViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_StarTeacherListViewController.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
#import "StarTeacherListViewCell.h"
#import "BMSQ_TeacherDetailViewController.h"
#import "BMSQ_AddStarViewController.h"
@interface BMSQ_StarTeacherListViewController ()<UICollectionViewDataSource, UICollectionViewDelegate>

@property (nonatomic, strong)UICollectionView * baseCollection;
@property (nonatomic, strong)UICollectionViewFlowLayout * layout;
@property (nonatomic, strong)NSMutableArray * dataSourceAry;
@property (nonatomic, assign)int  page;
@end

@implementation BMSQ_StarTeacherListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavTitle:@"名师堂"];
    [self setNavBackItem];
    [self customRightBtn];
    self.dataSourceAry = [@[] mutableCopy];
    self.page = 1;
    [self setViewUp];
    [self getShopTeacherList];
    
    NSNotificationCenter * defaultCenter1 = [NSNotificationCenter defaultCenter];
    [defaultCenter1 addObserver:self
                       selector:@selector(teacherDetailListNot:)
                           name:@"StarTeacherList"
                         object:nil];
    
    
}

- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44);
    //    [item setImage:[UIImage imageNamed:@"right_add"] forState:UIControlStateNormal];
    [item setTitle:@"增加" forState:UIControlStateNormal];
    item.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemAddClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}


- (void)setViewUp
{
    
    self.layout = [[UICollectionViewFlowLayout alloc] init];
    //设置每个item的大小
    self.layout.itemSize = CGSizeMake((APP_VIEW_WIDTH-20)/2, (APP_VIEW_WIDTH-20)/2 + 110);
    //设置列间距
    self.layout.minimumInteritemSpacing = 5;
    //设置行间距
    self.layout.minimumLineSpacing = 1;
    
    
    self.baseCollection = [[UICollectionView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 60) collectionViewLayout:self.layout];
    self.baseCollection.backgroundColor = APP_VIEW_BACKCOLOR;
    self.baseCollection.dataSource = self;
    self.baseCollection.delegate = self;
    [self.baseCollection addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [self.baseCollection addFooterWithTarget:self action:@selector(footerRereshing)];

    [self.view addSubview:self.baseCollection];
    
    // 注册cell
    [self.baseCollection registerClass:[StarTeacherListViewCell class] forCellWithReuseIdentifier:@"StarTeacherListViewCell"];
    
    
}

#pragma mark -------- UICollectionViewDataSource, UICollectionViewDelegate
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    if (self.dataSourceAry.count == 0) {
        return 0;
    }else{
        return self.dataSourceAry.count;
    }
    
    
}
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    StarTeacherListViewCell * cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"StarTeacherListViewCell" forIndexPath:indexPath];
    [cell setCellWithListValueDic:self.dataSourceAry[indexPath.row]];
    
    return cell;
}


- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    BMSQ_TeacherDetailViewController * teacherVC = [[BMSQ_TeacherDetailViewController alloc] init];
    teacherVC.nameStr = self.dataSourceAry[indexPath.row][@"teacherName"];
    teacherVC.teacherCode = self.dataSourceAry[indexPath.row][@"teacherCode"];
    teacherVC.number = 1;
    [self.navigationController pushViewController:teacherVC animated:YES];
}

//设置item的缩进量, 可根据不同的分区,设置不同的值
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(5, 5, 5, 5);
}
//返回行间距, 可根据不同分区设置不同的值
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{

    return 10;
}

#pragma mark -----  getShopTeacherList 名师堂列表
- (void)getShopTeacherList
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:self.page] forKey:@"page"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopTeacherList" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopTeacherList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        //        NSLog(@"%@", responseObject);
        
        [self.baseCollection headerEndRefreshing];
        [self.baseCollection footerEndRefreshing];
        
        if(self.page == 1) {
            [self.dataSourceAry removeAllObjects];
        }
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            [self.dataSourceAry addObjectsFromArray:responseObject[@"shopTeacherList"]];
        }
        
        if (self.dataSourceAry.count == 0) {
            CSAlert(@"暂无名师信息");
        }
        
        [self.baseCollection reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [self.baseCollection headerEndRefreshing];
        [self.baseCollection footerEndRefreshing];
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];
    
    
}


#pragma mark ---- 增加按钮
- (void)itemAddClick:(UIButton *)sender
{
    BMSQ_AddStarViewController * addStarVC = [[BMSQ_AddStarViewController alloc] init];
    [self.navigationController pushViewController:addStarVC animated:YES];
}

#pragma mark ---- 下拉刷新和上拉加载更多
- (void)headerRereshing{
    
    self.page = 1;
    [self getShopTeacherList];
}
- (void)footerRereshing{
    self.page ++;
    [self getShopTeacherList];
}

#pragma mark ------- 接收通知
#pragma mark ------ 接收通知
- (void)teacherDetailListNot:(NSNotification *)notification
{
    self.page = 1;
    [self getShopTeacherList];
}

- (void)dealloc
{
    
    // gatheringList
    NSNotificationCenter *gatheringList = [NSNotificationCenter defaultCenter];
    [gatheringList removeObserver:self
                             name:@"StarTeacherList"
                           object:nil];
    
}




#pragma mark ------ 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
